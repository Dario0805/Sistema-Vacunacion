package sena.adso.captcha.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sena.adso.captcha.dao.PacienteDAO;
import sena.adso.captcha.dto.Paciente;
import sena.adso.captcha.dto.Usuario;

/**
 * Servlet para gestionar pacientes
 */
public class PacienteServlet extends HttpServlet {

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
                cargarPacienteParaEditar(request, response);
                break;
            case "eliminar":
                eliminarPaciente(request, response);
                break;
            case "buscar":
                buscarPacientePorDocumento(request, response);
                break;
            default:
                listarPacientes(request, response);
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
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String documento = request.getParameter("documento");
        String fechaNacimientoStr = request.getParameter("fechaNacimiento");
        
        // Validar datos
        if (nombres == null || nombres.trim().isEmpty() || 
            apellidos == null || apellidos.trim().isEmpty() ||
            documento == null || documento.trim().isEmpty() ||
            fechaNacimientoStr == null || fechaNacimientoStr.trim().isEmpty()) {
            
            request.setAttribute("error", "Todos los campos obligatorios deben ser completados");
            request.getRequestDispatcher("/views/paciente_form.jsp").forward(request, response);
            return;
        }
        
        // Crear objeto Paciente con los datos del formulario
        Paciente paciente = new Paciente();
        paciente.setNombres(nombres);
        paciente.setApellidos(apellidos);
        paciente.setDocumento(documento);
        
        // Parsear fecha de nacimiento
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            paciente.setFechaNacimiento(sdf.parse(fechaNacimientoStr));
        } catch (ParseException ex) {
            request.setAttribute("error", "Formato de fecha inválido");
            request.setAttribute("paciente", paciente);
            request.getRequestDispatcher("/views/paciente_form.jsp").forward(request, response);
            return;
        }
        
        PacienteDAO pacienteDAO = new PacienteDAO();
        boolean exito;
        
        // Determinar si es inserción o actualización
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            paciente.setId(id);
            exito = pacienteDAO.actualizar(paciente);
            request.setAttribute("mensaje", exito ? "Paciente actualizado correctamente" : "Error al actualizar paciente");
        } else {
            exito = pacienteDAO.insertar(paciente);
            request.setAttribute("mensaje", exito ? "Paciente creado correctamente" : "Error al crear paciente");
        }
        
        // Redireccionar según resultado
        if (exito) {
            response.sendRedirect(request.getContextPath() + "/pacientes");
        } else {
            request.setAttribute("paciente", paciente);
            request.getRequestDispatcher("/views/paciente_form.jsp").forward(request, response);
        }
    }
    
    /**
     * Muestra la lista de pacientes
     */
    private void listarPacientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PacienteDAO pacienteDAO = new PacienteDAO();
        List<Paciente> pacientes = pacienteDAO.obtenerTodos();
        
        request.setAttribute("pacientes", pacientes);
        request.getRequestDispatcher("/views/pacientes.jsp").forward(request, response);
    }
    
    /**
     * Muestra el formulario para crear un nuevo paciente
     */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setAttribute("paciente", new Paciente());
        request.setAttribute("accion", "nuevo");
        request.getRequestDispatcher("/views/paciente_form.jsp").forward(request, response);
    }
    
    /**
     * Carga un paciente para editar
     */
    private void cargarPacienteParaEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            
            PacienteDAO pacienteDAO = new PacienteDAO();
            Paciente paciente = pacienteDAO.obtenerPorId(id);
            
            if (paciente != null) {
                request.setAttribute("paciente", paciente);
                request.setAttribute("accion", "editar");
                request.getRequestDispatcher("/views/paciente_form.jsp").forward(request, response);
                return;
            }
        }
        
        // Si no se encontró el paciente o no se proporcionó ID válido
        response.sendRedirect(request.getContextPath() + "/pacientes");
    }
    
    /**
     * Elimina un paciente
     */
    private void eliminarPaciente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            
            PacienteDAO pacienteDAO = new PacienteDAO();
            boolean exito = pacienteDAO.eliminar(id);
            
            if (exito) {
                request.setAttribute("mensaje", "Paciente eliminado correctamente");
            } else {
                request.setAttribute("error", "Error al eliminar paciente");
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/pacientes");
    }
    
    /**
     * Busca un paciente por número de documento
     */
    private void buscarPacientePorDocumento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String documento = request.getParameter("documento");
        if (documento != null && !documento.trim().isEmpty()) {
            PacienteDAO pacienteDAO = new PacienteDAO();
            Paciente paciente = pacienteDAO.obtenerPorDocumento(documento);
            
            if (paciente != null) {
                request.setAttribute("pacienteEncontrado", paciente);
            } else {
                request.setAttribute("error", "No se encontró ningún paciente con el documento especificado");
            }
        }
        
        request.getRequestDispatcher("/views/paciente_buscar.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet para gestionar pacientes";
    }
}
