package sena.adso.captcha.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet; // Añadido para mapeo
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
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     * Muestra el panel de control
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        // 1. Verificar si el usuario está autenticado
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // 2. Seguridad: Solo permitir acceso al personal médico o enfermero
        String rol = usuario.getRol();
        if (!"MEDICO".equals(rol) && !"ENFERMERO".equals(rol)) {
            // Si tiene un rol no autorizado, cerramos sesión y mandamos al login
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login?error=no_autorizado");
            return;
        }
        
        // 3. Preparar DAOs
        RegistroVacunacionDAO registroDAO = new RegistroVacunacionDAO();
        VacunaDAO vacunaDAO = new VacunaDAO();
        
        try {
            // 4. Obtener datos de la base de datos
            List<RegistroVacunacion> ultimosRegistros = registroDAO.obtenerTodos();
            List<Vacuna> vacunasDisponibles = vacunaDAO.obtenerTodas();
            
            // 5. Establecer atributos para el JSP
            // Pasamos las listas
            request.setAttribute("registros", ultimosRegistros);
            request.setAttribute("vacunas", vacunasDisponibles);
            
            // Pasamos contadores (si la lista es nula, ponemos 0)
            request.setAttribute("totalRegistros", (ultimosRegistros != null) ? ultimosRegistros.size() : 0);
            request.setAttribute("totalVacunas", (vacunasDisponibles != null) ? vacunasDisponibles.size() : 0);
            
            // Reforzamos los datos de sesión para el diseño del JSP
            session.setAttribute("usuarioNombre", usuario.getNombre());
            session.setAttribute("usuarioRol", usuario.getRol());

            // 6. Redirigir a la vista
            request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Manejo básico de errores de base de datos
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar datos del Dashboard");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // El dashboard es principalmente informativo, redirigimos al GET
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    @Override
    public String getServletInfo() {
        return "Servlet para el panel de control de SaludBoyaca";
    }
}
