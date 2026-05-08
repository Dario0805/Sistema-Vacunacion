package sena.adso.captcha.controller;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sena.adso.captcha.dao.UsuarioDAO;
import sena.adso.captcha.dto.Usuario;

public class LoginServlet extends HttpServlet {

    private static final SecureRandom OTP_RANDOM = new SecureRandom();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

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

        if (session.getAttribute("otpPending") != null && otpIngresado != null) {
            String otpReal = (String) session.getAttribute("otpCode");

            if (otpIngresado.equals(otpReal)) {
                Usuario usuario = (Usuario) session.getAttribute("tempUser");

                if (usuario == null) {
                    session.removeAttribute("otpPending");
                    session.removeAttribute("otpCode");
                    request.setAttribute("error", "La verificación expiró. Ingrese nuevamente.");
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

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.validarLogin(username, password);

        if (usuario != null) {
            if (requiereOtpAdministrador(usuario)) {
                String generatedOTP = String.valueOf(OTP_RANDOM.nextInt(900000) + 100000);

                try {
                    // RE-ACTIVADO: Envío real al correo
                    enviarOtpPorCorreo(usuario.getEmail(), generatedOTP);
                    
                    // Mantenemos el log por si necesitas verificar en consola
                    System.out.println("DEBUG OTP ENVIADO A " + usuario.getEmail() + ": " + generatedOTP);
                    
                    session.setAttribute("otpCode", generatedOTP);
                    session.setAttribute("tempUser", usuario);
                    session.setAttribute("otpPending", true);
                    response.sendRedirect(request.getContextPath() + "/login");
                    
                } catch (Exception e) {
                    System.err.println("Error enviando OTP: " + e.getMessage());
                    request.setAttribute("error", "Error al enviar el correo: " + e.getMessage());
                    request.getRequestDispatcher("/views/login.jsp").forward(request, response);
                }

            } else {
                iniciarSesion(session, usuario);
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }

        } else {
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    private boolean requiereOtpAdministrador(Usuario usuario) {
        String username = usuario.getUsername() != null ? usuario.getUsername().trim() : "";
        String rol = usuario.getRol() != null ? usuario.getRol().trim() : "";

        return "admin".equalsIgnoreCase(username)
                || "ADMIN".equalsIgnoreCase(rol)
                || "ADMINISTRADOR".equalsIgnoreCase(rol)
                || "MEDICO".equalsIgnoreCase(rol);
    }

    private void enviarOtpPorCorreo(String destinatario, String otp) throws MessagingException {
        final String correoRemitente = "clinipetadso@gmail.com";
        final String claveAplicacion = "qqzopsuxfmdcswmy";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        // Protocolos de seguridad para servidores en la nube
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        
        // Tiempos de espera aumentados para Render
        props.put("mail.smtp.connectiontimeout", "15000"); 
        props.put("mail.smtp.timeout", "15000");
        props.put("mail.smtp.writetimeout", "15000");

        jakarta.mail.Session mailSession = jakarta.mail.Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoRemitente, claveAplicacion);
            }
        });

        Message mensaje = new MimeMessage(mailSession);
        mensaje.setFrom(new InternetAddress(correoRemitente));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        mensaje.setSubject("Tu código OTP de seguridad");
        mensaje.setText("Hola,\n\nTu código de verificación es: " + otp + "\n\nSi no solicitaste este código, ignora este mensaje.");

        Transport.send(mensaje);
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
        return "Servlet para gestionar el inicio de sesión con seguridad OTP para administrador";
    }
}
