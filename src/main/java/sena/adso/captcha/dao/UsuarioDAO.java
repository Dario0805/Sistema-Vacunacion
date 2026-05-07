package sena.adso.captcha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sena.adso.captcha.dto.Usuario;
import sena.adso.captcha.model.Conexion;

public class UsuarioDAO {

    public boolean insertar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            conn = Conexion.getConnection();
            String sql = "INSERT INTO usuarios (nombres, apellidos, documento, email, username, password, rol, especialidad, institucion) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            System.err.println("Error al insertar usuario: " + ex.getMessage());
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
            String sql = "UPDATE usuarios SET nombres=?, apellidos=?, documento=?, email=?, "
                    + "username=?, password=?, rol=?, especialidad=?, institucion=? WHERE id=?";

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
            System.err.println("Error al actualizar usuario: " + ex.getMessage());
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
            String sql = "DELETE FROM usuarios WHERE id=?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            exito = stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Error al eliminar usuario: " + ex.getMessage());
        } finally {
            cerrar(null, stmt, conn);
        }

        return exito;
    }

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
            System.err.println("Error al obtener usuario: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }

        return usuario;
    }

    public Usuario validarLogin(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            username = username != null ? username.trim() : "";
            password = password != null ? password.trim() : "";

            System.out.println("Intentando login...");
            System.out.println("Usuario recibido: [" + username + "]");
            System.out.println("Password recibido: [" + password + "]");

            conn = Conexion.getConnection();
            System.out.println("Conexion OK");

            String sql = "SELECT * FROM usuarios "
                    + "WHERE LTRIM(RTRIM(username)) = ? "
                    + "AND LTRIM(RTRIM(password)) = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Usuario encontrado");
                usuario = mapearUsuario(rs);
            } else {
                System.out.println("Usuario NO encontrado");
            }

        } catch (SQLException ex) {
            System.err.println("Error al validar login: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }

        return usuario;
    }

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
            System.err.println("Error al obtener usuarios: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }

        return usuarios;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNombres(rs.getString("nombres"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setDocumento(rs.getString("documento"));
        usuario.setEmail(rs.getString("email"));
        usuario.setUsername(rs.getString("username"));
        usuario.setPassword(rs.getString("password"));
        usuario.setRol(rs.getString("rol"));
        usuario.setEspecialidad(rs.getString("especialidad"));
        usuario.setInstitucion(rs.getString("institucion"));
        return usuario;
    }

    private void cerrar(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            Conexion.closeConnection(conn);
        } catch (SQLException ex) {
            System.err.println("Error al cerrar la conexión: " + ex.getMessage());
        }
    }
}