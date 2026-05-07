package sena.adso.captcha.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sena.adso.captcha.dao.VacunaDAO;
import sena.adso.captcha.dto.Usuario;
import sena.adso.captcha.dto.Vacuna;

/**
 * Servlet para gestionar vacunas
 */
public class VacunaServlet extends HttpServlet {

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
        
        HttpSession session = request.getSession();
        Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
        
        // Verificar si el usuario está autenticado
        if (usuarioActual == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Obtener parámetros y determinar acción
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }
        
        switch (accion) {
            case "nuevo":
                mostrarFormulario(request, response);
                break;
            case "editar":
                cargarVacunaParaEditar(request, response);
                break;
            case "eliminar":
                eliminarVacuna(request, response);
                break;
            default:
                listarVacunas(request, response);
                break;
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
        
        HttpSession session = request.getSession();
        Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
        
        // Verificar si el usuario está autenticado
        if (usuarioActual == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Obtener parámetros del formulario
        String idParam = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String lote = request.getParameter("lote");
        String laboratorio = request.getParameter("laboratorio");
        String fechaVencimiento = request.getParameter("fechaVencimiento");
        
        // Validar datos
        if (nombre == null || nombre.trim().isEmpty() || 
            lote == null || lote.trim().isEmpty() ||
            laboratorio == null || laboratorio.trim().isEmpty()) {
            
            request.setAttribute("error", "Todos los campos obligatorios deben ser completados");
            request.getRequestDispatcher("/views/vacuna_form.jsp").forward(request, response);
            return;
        }
        
        // Crear objeto Vacuna con los datos del formulario
        Vacuna vacuna = new Vacuna();
        vacuna.setNombre(nombre);
        vacuna.setLote(lote);
        vacuna.setLaboratorio(laboratorio);
        vacuna.setFechaVencimiento(fechaVencimiento);
        
        VacunaDAO vacunaDAO = new VacunaDAO();
        boolean exito;
        
        // Determinar si es inserción o actualización
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            vacuna.setId(id);
            exito = vacunaDAO.actualizar(vacuna);
            request.setAttribute("mensaje", exito ? "Vacuna actualizada correctamente" : "Error al actualizar vacuna");
        } else {
            exito = vacunaDAO.insertar(vacuna);
            request.setAttribute("mensaje", exito ? "Vacuna creada correctamente" : "Error al crear vacuna");
        }
        
        // Redireccionar según resultado
        if (exito) {
            response.sendRedirect(request.getContextPath() + "/vacunas");
        } else {
            request.setAttribute("vacuna", vacuna);
            request.getRequestDispatcher("/views/vacuna_form.jsp").forward(request, response);
        }
    }
    
    /**
     * Muestra la lista de vacunas
     */
    private void listarVacunas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        VacunaDAO vacunaDAO = new VacunaDAO();
        List<Vacuna> vacunas = vacunaDAO.obtenerTodas();
        
        request.setAttribute("vacunas", vacunas);
        request.getRequestDispatcher("/views/vacunas.jsp").forward(request, response);
    }
    
    /**
     * Muestra el formulario para crear una nueva vacuna
     */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setAttribute("vacuna", new Vacuna());
        request.setAttribute("accion", "nuevo");
        request.getRequestDispatcher("/views/vacuna_form.jsp").forward(request, response);
    }
    
    /**
     * Carga una vacuna para editar
     */
    private void cargarVacunaParaEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            
            VacunaDAO vacunaDAO = new VacunaDAO();
            Vacuna vacuna = vacunaDAO.obtenerPorId(id);
            
            if (vacuna != null) {
                request.setAttribute("vacuna", vacuna);
                request.setAttribute("accion", "editar");
                request.getRequestDispatcher("/views/vacuna_form.jsp").forward(request, response);
                return;
            }
        }
        
        // Si no se encontró la vacuna o no se proporcionó ID válido
        response.sendRedirect(request.getContextPath() + "/vacunas");
    }
    
    /**
     * Elimina una vacuna
     */
    private void eliminarVacuna(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            
            VacunaDAO vacunaDAO = new VacunaDAO();
            boolean exito = vacunaDAO.eliminar(id);
            
            if (exito) {
                request.setAttribute("mensaje", "Vacuna eliminada correctamente");
            } else {
                request.setAttribute("error", "Error al eliminar vacuna");
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/vacunas");
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet para gestionar vacunas";
    }
}
