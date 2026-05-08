package sena.adso.captcha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sena.adso.captcha.dto.Usuario;
import sena.adso.captcha.model.Conexion;
// BCrypt eliminado para usar texto plano

public class UsuarioDAO {

    /**
     * Valida el login comparando la clave en texto plano
     */
    public Usuario validarLogin(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            username = (username != null) ? username.trim() : "";
            password = (password != null) ? password.trim() : "";
            
            conn = Conexion.getConnection();
            
            // Comparamos directamente username y password en la consulta
            String sql = "SELECT * FROM public.usuarios WHERE username = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = mapearUsuario(rs);
            } else {
                System.out.println("⚠️ Intento de login fallido para el usuario: " + username);
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error en validarLogin: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }
        return usuario;
    }

    public boolean insertar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        try {
            conn = Conexion.getConnection();
            String sql = "INSERT INTO public.usuarios (nombres, apellidos, documento, email, username, password, rol, especialidad, institucion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNombres());
            stmt.setString(2, usuario.getApellidos());
            stmt.setString(3, usuario.getDocumento());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getUsername());
            // Guardamos la contraseña tal cual llega (texto plano)
            stmt.setString(6, usuario.getPassword()); 
            stmt.setString(7, usuario.getRol());
            stmt.setString(8, usuario.getEspecialidad());
            stmt.setString(9, usuario.getInstitucion());
            exito = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("❌ Error al insertar: " + ex.getMessage());
        } finally {
            cerrar(null, stmt, conn);
        }
        return exito;
    }

    public boolean actualizar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        try {
            conn = Conexion.getConnection();
            String sql = "UPDATE public.usuarios SET nombres=?, apellidos=?, documento=?, email=?, username=?, password=?, rol=?, especialidad=?, institucion=? WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNombres());
            stmt.setString(2, usuario.getApellidos());
            stmt.setString(3, usuario.getDocumento());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getUsername());
            stmt.setString(6, usuario.getPassword()); // Texto plano
            stmt.setString(7, usuario.getRol());
            stmt.setString(8, usuario.getEspecialidad());
            stmt.setString(9, usuario.getInstitucion());
            stmt.setInt(10, usuario.getId());
            exito = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("❌ Error al actualizar: " + ex.getMessage());
        } finally {
            cerrar(null, stmt, conn);
        }
        return exito;
    }

    public boolean eliminar(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        try {
            conn = Conexion.getConnection();
            String sql = "DELETE FROM public.usuarios WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            exito = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("❌ Error al eliminar: " + ex.getMessage());
        } finally {
            cerrar(null, stmt, conn);
        }
        return exito;
    }

    public List<Usuario> obtenerTodos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Usuario> usuarios = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM public.usuarios ORDER BY apellidos, nombres";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error al listar: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }
        return usuarios;
    }

    public Usuario obtenerPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM public.usuarios WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                usuario = mapearUsuario(rs);
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error al buscar por ID: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }
        return usuario;
    }

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
