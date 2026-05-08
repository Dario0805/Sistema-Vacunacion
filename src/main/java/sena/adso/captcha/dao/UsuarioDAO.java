package sena.adso.captcha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sena.adso.captcha.dto.Usuario;
import sena.adso.captcha.model.Conexion;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {

    // --- 1. VALIDAR LOGIN (Usado por LoginServlet) ---
    public Usuario validarLogin(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            username = (username != null) ? username.trim() : "";
            password = (password != null) ? password.trim() : "";
            conn = Conexion.getConnection();
            
            String sql = "SELECT * FROM usuarios WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPass = rs.getString("password");
                // Compara password plano con el hash de la DB
                if (BCrypt.checkpw(password, hashedPass)) {
                    usuario = mapearUsuario(rs);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error en validarLogin: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }
        return usuario;
    }

    // --- 2. INSERTAR (Usado por UsuarioServlet) ---
    public boolean insertar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        try {
            conn = Conexion.getConnection();
            String sql = "INSERT INTO usuarios (nombres, apellidos, documento, email, username, password, rol, especialidad, institucion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNombres());
            stmt.setString(2, usuario.getApellidos());
            stmt.setString(3, usuario.getDocumento());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getUsername());
            stmt.setString(6, usuario.getPassword()); 
            stmt.setString(7, usuario.getRol());
            stmt.setString(8, usuario.getEspecialidad());
            stmt.setString(9, usuario.getInstitucion());
            exito = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al insertar: " + ex.getMessage());
        } finally {
            cerrar(null, stmt, conn);
        }
        return exito;
    }

    // --- 3. ACTUALIZAR (Usado por UsuarioServlet) ---
    public boolean actualizar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        try {
            conn = Conexion.getConnection();
            String sql = "UPDATE usuarios SET nombres=?, apellidos=?, documento=?, email=?, username=?, password=?, rol=?, especialidad=?, institucion=? WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNombres());
            stmt.setString(2, usuario.getApellidos());
            stmt.setString(3, usuario.getDocumento());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getUsername());
            stmt.setString(6, usuario.getPassword());
            stmt.setString(7, usuario.getRol());
            stmt.setString(8, usuario.getEspecialidad());
            stmt.setString(9, usuario.getInstitucion());
            stmt.setInt(10, usuario.getId());
            exito = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al actualizar: " + ex.getMessage());
        } finally {
            cerrar(null, stmt, conn);
        }
        return exito;
    }

    // --- 4. ELIMINAR (Usado por UsuarioServlet) ---
    public boolean eliminar(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        try {
            conn = Conexion.getConnection();
            String sql = "DELETE FROM usuarios WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            exito = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al eliminar: " + ex.getMessage());
        } finally {
            cerrar(null, stmt, conn);
        }
        return exito;
    }

    // --- 5. OBTENER TODOS (Usado por UsuarioServlet) ---
    public List<Usuario> obtenerTodos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Usuario> usuarios = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM usuarios ORDER BY apellidos, nombres";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener todos: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }
        return usuarios;
    }

    // --- 6. OBTENER POR ID (Usado por UsuarioServlet) ---
    public Usuario obtenerPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM usuarios WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                usuario = mapearUsuario(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener por id: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }
        return usuario;
    }

    // --- MÉTODOS DE APOYO INTERNOS ---
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombres(rs.getString("nombres"));
        u.setApellidos(rs.getString("apellidos"));
        u.setDocumento(rs.getString("documento"));
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRol(rs.getString("rol"));
        u.setEspecialidad(rs.getString("especialidad"));
        u.setInstitucion(rs.getString("institucion"));
        return u;
    }

    private void cerrar(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) Conexion.closeConnection(conn);
        } catch (SQLException ex) {
            System.err.println("Error al cerrar recursos: " + ex.getMessage());
        }
    }
}
