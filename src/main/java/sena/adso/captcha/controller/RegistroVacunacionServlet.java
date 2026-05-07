package sena.adso.captcha.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sena.adso.captcha.dao.PacienteDAO;
import sena.adso.captcha.dao.RegistroVacunacionDAO;
import sena.adso.captcha.dao.VacunaDAO;
import sena.adso.captcha.dto.Paciente;
import sena.adso.captcha.dto.RegistroVacunacion;
import sena.adso.captcha.dto.Usuario;
import sena.adso.captcha.dto.Vacuna;
import sena.adso.captcha.util.ExcelGenerator;

/**
 * Servlet para gestionar registros de vacunación
 */
public class RegistroVacunacionServlet extends HttpServlet {

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
                cargarRegistroParaEditar(request, response);
                break;
            case "eliminar":
                eliminarRegistro(request, response);
                break;
            case "exportar":
                exportarExcel(request, response);
                break;
            default:
                listarRegistros(request, response);
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
        String idPacienteParam = request.getParameter("idPaciente");
        String idVacunaParam = request.getParameter("idVacuna");
        String fechaVacunacionStr = request.getParameter("fechaVacunacion");
        String lugarVacunacion = request.getParameter("lugarVacunacion");
        String observaciones = request.getParameter("observaciones");
        
        // Validar datos
        if (idPacienteParam == null || idPacienteParam.trim().isEmpty() || 
            idVacunaParam == null || idVacunaParam.trim().isEmpty() ||
            fechaVacunacionStr == null || fechaVacunacionStr.trim().isEmpty() ||
            lugarVacunacion == null || lugarVacunacion.trim().isEmpty()) {
            
            request.setAttribute("error", "Todos los campos obligatorios deben ser completados");
            
            // Cargar datos para el formulario
            cargarDatosFormulario(request);
            
            request.getRequestDispatcher("/views/registro_form.jsp").forward(request, response);
            return;
        }
        
        // Crear objeto RegistroVacunacion con los datos del formulario
        RegistroVacunacion registro = new RegistroVacunacion();
        registro.setIdPaciente(Integer.parseInt(idPacienteParam));
        registro.setIdVacuna(Integer.parseInt(idVacunaParam));
        registro.setLugarVacunacion(lugarVacunacion);
        registro.setObservaciones(observaciones != null ? observaciones : "");
        registro.setIdPersonalMedico(usuarioActual.getId());
        
        // Parsear fecha de vacunación
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            registro.setFechaVacunacion(sdf.parse(fechaVacunacionStr));
        } catch (ParseException ex) {
            request.setAttribute("error", "Formato de fecha inválido");
            
            // Cargar datos para el formulario
            cargarDatosFormulario(request);
            request.setAttribute("registro", registro);
            
            request.getRequestDispatcher("/views/registro_form.jsp").forward(request, response);
            return;
        }
        
        RegistroVacunacionDAO registroDAO = new RegistroVacunacionDAO();
        boolean exito;
        
        // Determinar si es inserción o actualización
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            registro.setId(id);
            exito = registroDAO.actualizar(registro);
            request.setAttribute("mensaje", exito ? "Registro actualizado correctamente" : "Error al actualizar registro");
        } else {
            exito = registroDAO.insertar(registro);
            request.setAttribute("mensaje", exito ? "Registro creado correctamente" : "Error al crear registro");
        }
        
        // Redireccionar según resultado
        if (exito) {
            response.sendRedirect(request.getContextPath() + "/registros");
        } else {
            // Cargar datos para el formulario
            cargarDatosFormulario(request);
            request.setAttribute("registro", registro);
            
            request.getRequestDispatcher("/views/registro_form.jsp").forward(request, response);
        }
    }
    
    /**
     * Muestra la lista de registros de vacunación
     */
    private void listarRegistros(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        RegistroVacunacionDAO registroDAO = new RegistroVacunacionDAO();
        List<RegistroVacunacion> registros = registroDAO.obtenerTodos();
        
        request.setAttribute("registros", registros);
        request.getRequestDispatcher("/views/registros.jsp").forward(request, response);
    }
    
    /**
     * Muestra el formulario para crear un nuevo registro
     */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cargar datos para el formulario
        cargarDatosFormulario(request);
        
        request.setAttribute("registro", new RegistroVacunacion());
        request.setAttribute("accion", "nuevo");
        request.getRequestDispatcher("/views/registro_form.jsp").forward(request, response);
    }
    
    /**
     * Carga un registro para editar
     */
    private void cargarRegistroParaEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            
            RegistroVacunacionDAO registroDAO = new RegistroVacunacionDAO();
            RegistroVacunacion registro = registroDAO.obtenerPorId(id);
            
            if (registro != null) {
                // Cargar datos para el formulario
                cargarDatosFormulario(request);
                
                request.setAttribute("registro", registro);
                request.setAttribute("accion", "editar");
                request.getRequestDispatcher("/views/registro_form.jsp").forward(request, response);
                return;
            }
        }
        
        // Si no se encontró el registro o no se proporcionó ID válido
        response.sendRedirect(request.getContextPath() + "/registros");
    }
    
    /**
     * Elimina un registro
     */
    private void eliminarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            
            HttpSession session = request.getSession();
            Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
            
            RegistroVacunacionDAO registroDAO = new RegistroVacunacionDAO();
            boolean exito = registroDAO.eliminar(id, usuarioActual.getId());
            
            if (exito) {
                request.setAttribute("mensaje", "Registro eliminado correctamente");
            } else {
                request.setAttribute("error", "Error al eliminar registro");
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/registros");
    }
    
    /**
     * Exporta los registros a Excel
     */
    private void exportarExcel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        RegistroVacunacionDAO registroDAO = new RegistroVacunacionDAO();
        List<RegistroVacunacion> registros = registroDAO.obtenerTodos();
        
        byte[] excel = ExcelGenerator.generarExcelVacunacion(registros);
        
        // Configurar la respuesta
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=registros_vacunacion.xlsx");
        response.setContentLength(excel.length);
        
        // Escribir el Excel en la respuesta
        try (OutputStream out = response.getOutputStream()) {
            out.write(excel);
            out.flush();
        }
    }
    
    /**
     * Carga los datos necesarios para el formulario (pacientes y vacunas)
     */
    private void cargarDatosFormulario(HttpServletRequest request) {
        // Cargar lista de pacientes
        PacienteDAO pacienteDAO = new PacienteDAO();
        List<Paciente> pacientes = pacienteDAO.obtenerTodos();
        request.setAttribute("pacientes", pacientes);
        
        // Cargar lista de vacunas
        VacunaDAO vacunaDAO = new VacunaDAO();
        List<Vacuna> vacunas = vacunaDAO.obtenerTodas();
        request.setAttribute("vacunas", vacunas);
        
        // Fecha actual para el formulario
        request.setAttribute("fechaActual", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date()));
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet para gestionar registros de vacunación";
    }
}
