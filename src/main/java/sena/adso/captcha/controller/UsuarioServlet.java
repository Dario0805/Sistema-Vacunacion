package sena.adso.captcha.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sena.adso.captcha.dao.UsuarioDAO;
import sena.adso.captcha.dto.Usuario;

/**
 * Servlet para gestionar usuarios (personal médico)
 */
public class UsuarioServlet extends HttpServlet {

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
                cargarUsuarioParaEditar(request, response);
                break;
            case "eliminar":
                eliminarUsuario(request, response);
                break;
            default:
                listarUsuarios(request, response);
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
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rol = request.getParameter("rol");
        String especialidad = request.getParameter("especialidad");
        String institucion = request.getParameter("institucion");
        
        // Validar datos
        if (nombres == null || nombres.trim().isEmpty() || 
            apellidos == null || apellidos.trim().isEmpty() ||
            documento == null || documento.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            rol == null || rol.trim().isEmpty()) {
            
            request.setAttribute("error", "Todos los campos obligatorios deben ser completados");
            request.getRequestDispatcher("/views/usuario_form.jsp").forward(request, response);
            return;
        }
        
        // Crear objeto Usuario con los datos del formulario
        Usuario usuario = new Usuario();
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setDocumento(documento);
        usuario.setEmail(email);
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setRol(rol);
        usuario.setEspecialidad(especialidad);
        usuario.setInstitucion(institucion);
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        boolean exito;
        
        // Determinar si es inserción o actualización
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            usuario.setId(id);
            exito = usuarioDAO.actualizar(usuario);
            request.setAttribute("mensaje", exito ? "Usuario actualizado correctamente" : "Error al actualizar usuario");
        } else {
            exito = usuarioDAO.insertar(usuario);
            request.setAttribute("mensaje", exito ? "Usuario creado correctamente" : "Error al crear usuario");
        }
        
        // Redireccionar según resultado
        if (exito) {
            response.sendRedirect(request.getContextPath() + "/usuarios");
        } else {
            request.setAttribute("usuario", usuario);
            request.getRequestDispatcher("/views/usuario_form.jsp").forward(request, response);
        }
    }
    
    /**
     * Muestra la lista de usuarios
     */
    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        List<Usuario> usuarios = usuarioDAO.obtenerTodos();
        
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/views/usuarios.jsp").forward(request, response);
    }
    
    /**
     * Muestra el formulario para crear un nuevo usuario
     */
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setAttribute("usuario", new Usuario());
        request.setAttribute("accion", "nuevo");
        request.getRequestDispatcher("/views/usuario_form.jsp").forward(request, response);
    }
    
    /**
     * Carga un usuario para editar
     */
    private void cargarUsuarioParaEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.obtenerPorId(id);
            
            if (usuario != null) {
                request.setAttribute("usuario", usuario);
                request.setAttribute("accion", "editar");
                request.getRequestDispatcher("/views/usuario_form.jsp").forward(request, response);
                return;
            }
        }
        
        // Si no se encontró el usuario o no se proporcionó ID válido
        response.sendRedirect(request.getContextPath() + "/usuarios");
    }
    
    /**
     * Elimina un usuario
     */
    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            boolean exito = usuarioDAO.eliminar(id);
            
            if (exito) {
                request.setAttribute("mensaje", "Usuario eliminado correctamente");
            } else {
                request.setAttribute("error", "Error al eliminar usuario");
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/usuarios");
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet para gestionar usuarios (personal médico)";
    }
}
