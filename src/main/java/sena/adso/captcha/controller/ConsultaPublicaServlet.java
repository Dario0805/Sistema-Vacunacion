package sena.adso.captcha.controller;

import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sena.adso.captcha.dao.PacienteDAO;
import sena.adso.captcha.dao.RegistroVacunacionDAO;
import sena.adso.captcha.dto.Paciente;
import sena.adso.captcha.dto.RegistroVacunacion;
import sena.adso.captcha.util.CaptchaGenerator;
import sena.adso.captcha.util.PDFGenerator;

/**
 * Servlet para la consulta pública de historial de vacunación
 */
public class ConsultaPublicaServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if (accion != null && accion.equals("descargar")) {
            descargarCertificado(request, response);
        } else {
            // Generar un nuevo CAPTCHA para el formulario de consulta
            String captchaText = CaptchaGenerator.generarTextoCaptcha();
            String captchaImage = CaptchaGenerator.generarImagenCaptcha(captchaText);
            
            // Guardar el texto del CAPTCHA en la sesión para validarlo después
            HttpSession session = request.getSession();
            session.setAttribute("captchaText", captchaText);
            
            // Enviar la imagen del CAPTCHA a la página JSP
            request.setAttribute("captchaImage", captchaImage);
            
            // Redirigir a la página de consulta pública
            request.getRequestDispatcher("/views/consulta_publica.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String documento = request.getParameter("documento");
        String captchaIngresado = request.getParameter("captcha");
        
        HttpSession session = request.getSession();
        String captchaReal = (String) session.getAttribute("captchaText");
        
        // Validar CAPTCHA
        if (!CaptchaGenerator.validarCaptcha(captchaIngresado, captchaReal)) {
            request.setAttribute("error", "El código CAPTCHA ingresado es incorrecto");
            
            // Generar un nuevo CAPTCHA
            String nuevoCaptchaText = CaptchaGenerator.generarTextoCaptcha();
            String nuevoCaptchaImage = CaptchaGenerator.generarImagenCaptcha(nuevoCaptchaText);
            session.setAttribute("captchaText", nuevoCaptchaText);
            request.setAttribute("captchaImage", nuevoCaptchaImage);
            
            request.getRequestDispatcher("/views/consulta_publica.jsp").forward(request, response);
            return;
        }
        
        // Buscar paciente por documento
        if (documento != null && !documento.trim().isEmpty()) {
            PacienteDAO pacienteDAO = new PacienteDAO();
            Paciente paciente = pacienteDAO.obtenerPorDocumento(documento);
            
            if (paciente != null) {
                // Obtener historial de vacunación
                RegistroVacunacionDAO registroDAO = new RegistroVacunacionDAO();
                List<RegistroVacunacion> registros = registroDAO.obtenerPorDocumentoPaciente(documento);
                
                request.setAttribute("paciente", paciente);
                request.setAttribute("registros", registros);
                session.setAttribute("documentoConsultado", documento);
            } else {
                request.setAttribute("error", "No se encontró ningún paciente con el documento especificado");
                
                // Generar un nuevo CAPTCHA
                String nuevoCaptchaText = CaptchaGenerator.generarTextoCaptcha();
                String nuevoCaptchaImage = CaptchaGenerator.generarImagenCaptcha(nuevoCaptchaText);
                session.setAttribute("captchaText", nuevoCaptchaText);
                request.setAttribute("captchaImage", nuevoCaptchaImage);
            }
        }
        
        request.getRequestDispatcher("/views/consulta_publica.jsp").forward(request, response);
    }
    
    /**
     * Genera y descarga un certificado PDF de vacunación
     */
    private void descargarCertificado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String documento = (String) session.getAttribute("documentoConsultado");
        
        if (documento != null && !documento.trim().isEmpty()) {
            PacienteDAO pacienteDAO = new PacienteDAO();
            Paciente paciente = pacienteDAO.obtenerPorDocumento(documento);
            
            if (paciente != null) {
                // Obtener historial de vacunación
                RegistroVacunacionDAO registroDAO = new RegistroVacunacionDAO();
                List<RegistroVacunacion> registros = registroDAO.obtenerPorDocumentoPaciente(documento);
                
                try {
                    // Generar PDF
                    byte[] pdfBytes = PDFGenerator.generarCertificadoVacunacion(paciente, registros);
                    
                    // Configurar la respuesta
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "attachment; filename=certificado_vacunacion.pdf");
                    response.setContentLength(pdfBytes.length);
                    
                    // Escribir el PDF en la respuesta
                    try (OutputStream out = response.getOutputStream()) {
                        out.write(pdfBytes);
                        out.flush();
                    }
                    
                    return;
                } catch (DocumentException ex) {
                    request.setAttribute("error", "Error al generar el certificado: " + ex.getMessage());
                }
            } else {
                request.setAttribute("error", "No se encontró ningún paciente con el documento especificado");
            }
        } else {
            request.setAttribute("error", "Debe consultar un documento primero");
        }
        
        // Si hay error, volver a la página de consulta
        doGet(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet para la consulta pública de historial de vacunación";
    }
}
