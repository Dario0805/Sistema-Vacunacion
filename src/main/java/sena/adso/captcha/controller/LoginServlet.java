package sena.adso.captcha.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sena.adso.captcha.dao.UsuarioDAO;
import sena.adso.captcha.dto.Usuario;

/**
 * Servlet para gestionar el inicio de sesión con seguridad OTP mediante API Brevo.
 */
public class LoginServlet extends HttpServlet {

    private static final SecureRandom OTP_RANDOM = new SecureRandom();
    
    // CLAVE API BREVO SUMINISTRADA
    private static final String BREVO_API_KEY = "xsmtpsib-d03466bc5718d99f95b9a6bd7306a748d5c67b128e836bface24e4e1e7a31645-aBv6IGyjQ0W5vpzg";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Si se solicita reset, limpiamos la sesión de intentos previos
        if (request.getParameter("reset") != null) {
            session.removeAttribute("otpPending");
            session.removeAttribute("otpCode");
            session.removeAttribute("tempUser");
        }

        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String otpIngresado = request.getParameter("otp");

        // --- CASO 1: VERIFICACIÓN DEL CÓDIGO OTP ---
        if (session.getAttribute("otpPending") != null && otpIngresado != null) {
            String otpReal = (String) session.getAttribute("otpCode");

            if (otpIngresado.equals(otpReal)) {
                Usuario usuario = (Usuario) session.getAttribute("tempUser");

                if (usuario == null) {
                    session.removeAttribute("otpPending");
                    session.removeAttribute("otpCode");
                    request.setAttribute("error", "La sesión expiró. Ingrese de nuevo.");
                    request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                    return;
                }

                iniciarSesion(session, usuario);
                response.sendRedirect(request.getContextPath() + "/dashboard");

            } else {
                request.setAttribute("error", "El código OTP ingresado es incorrecto");
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            }
            return;
        }

        // --- CASO 2: LOGIN INICIAL (USUARIO Y CONTRASEÑA) ---
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.validarLogin(username, password);

        if (usuario != null) {
            if (requiereOtpAdministrador(usuario)) {
                // Generar código de 6 dígitos
                String generatedOTP = String.valueOf(OTP_RANDOM.nextInt(900000) + 100000);

                try {
                    // Envío real al correo usando la API REST de Brevo (Puerto 443)
                    enviarOtpVíaApi(usuario.getEmail(), generatedOTP);
                    
                    // Backup en consola de Render por si falla la red
                    System.out.println("******************************************");
                    System.out.println("DEBUG OTP PARA " + usuario.getEmail() + ": " + generatedOTP);
                    System.out.println("******************************************");
                    
                    session.setAttribute("otpCode", generatedOTP);
                    session.setAttribute("tempUser", usuario);
                    session.setAttribute("otpPending", true);
                    
                    // Redirigir a la misma vista de login que ahora mostrará el campo OTP
                    response.sendRedirect(request.getContextPath() + "/login");
                    
                } catch (Exception e) {
                    System.err.println("Error procesando seguridad: " + e.getMessage());
                    request.setAttribute("error", "Error en el servidor de correo.");
                    request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                }

            } else {
                // Si no es admin/médico, entra directo
                iniciarSesion(session, usuario);
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }

        } else {
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    private boolean requiereOtpAdministrador(Usuario usuario) {
        String username = (usuario.getUsername() != null) ? usuario.getUsername().trim() : "";
        String rol = (usuario.getRol() != null) ? usuario.getRol().trim() : "";

        return "admin".equalsIgnoreCase(username)
                || "ADMIN".equalsIgnoreCase(rol)
                || "ADMINISTRADOR".equalsIgnoreCase(rol)
                || "MEDICO".equalsIgnoreCase(rol);
    }

    /**
     * Realiza una petición POST a la API de Brevo para enviar el correo.
     * Al ser tráfico HTTPS (Puerto 443), Render no lo bloquea.
     */
    private void enviarOtpVíaApi(String destinatario, String otp) {
        try {
            URL url = new URL("https://api.brevo.com/v3/smtp/email");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("api-key", BREVO_API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String emailLimpio = destinatario.trim().toLowerCase();

            String jsonPayload = "{"
                + "\"sender\":{\"name\":\"Sistema Clinipet\",\"email\":\"clinipetadso@gmail.com\"},"
                + "\"to\":[{\"email\":\"" + emailLimpio + "\"}],"
                + "\"subject\":\"Código de Seguridad OTP\","
                + "\"htmlContent\":\"<html><body>"
                + "<h2>Verificación de Acceso</h2>"
                + "<p>Tu código de seguridad para ingresar al sistema es: <b>" + otp + "</b></p>"
                + "<p>Si no solicitaste este código, ignora este mensaje.</p>"
                + "</body></html>\""
                + "}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("✅ Correo enviado exitosamente vía API Brevo");
            } else {
                System.err.println("❌ Error de API Brevo. Código de respuesta: " + responseCode);
            }

        } catch (Exception e) {
            System.err.println("❌ Excepción al enviar por API: " + e.getMessage());
        }
    }

    private void iniciarSesion(HttpSession session, Usuario usuario) {
        session.setAttribute("usuario", usuario);
        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("usuarioNombre", usuario.getNombres() + " " + usuario.getApellidos());
        session.setAttribute("usuarioRol", usuario.getRol());

        session.removeAttribute("otpPending");
        session.removeAttribute("otpCode");
        session.removeAttribute("tempUser");
    }

    @Override
    public String getServletInfo() {
        return "Servlet de Login con seguridad OTP mediante API REST";
    }
}
