package sena.adso.captcha.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sena.adso.captcha.dao.RegistroVacunacionDAO;
import sena.adso.captcha.dao.VacunaDAO;
import sena.adso.captcha.dto.RegistroVacunacion;
import sena.adso.captcha.dto.Usuario;
import sena.adso.captcha.dto.Vacuna;

/**
 * Servlet para el panel de control
 */
public class DashboardServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     * Muestra el panel de control
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        // Verificar si el usuario está autenticado
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Solo permitir acceso al personal médico
        if (!"MEDICO".equals(usuario.getRol()) && !"ENFERMERO".equals(usuario.getRol())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Obtener estadísticas para el dashboard
        RegistroVacunacionDAO registroDAO = new RegistroVacunacionDAO();
        VacunaDAO vacunaDAO = new VacunaDAO();
        
        // Obtener todos los registros para mostrar en tabla
        List<RegistroVacunacion> ultimosRegistros = registroDAO.obtenerTodos();
        List<Vacuna> vacunasDisponibles = vacunaDAO.obtenerTodas();
        
        request.setAttribute("registros", ultimosRegistros);
        request.setAttribute("vacunas", vacunasDisponibles);
        request.setAttribute("totalRegistros", ultimosRegistros.size());
        request.setAttribute("totalVacunas", vacunasDisponibles.size());
        
        // Redirigir al panel de control
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
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
        // Por ahora, el dashboard no procesa peticiones POST
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet para el panel de control";
    }
}
