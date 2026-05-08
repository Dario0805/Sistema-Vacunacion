package sena.adso.captcha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sena.adso.captcha.dto.Usuario;
import sena.adso.captcha.model.Conexion;
import org.mindrot.jbcrypt.BCrypt; // Importación necesaria

public class UsuarioDAO {

    public Usuario validarLogin(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            username = (username != null) ? username.trim() : "";
            password = (password != null) ? password.trim() : "";

            conn = Conexion.getConnection();
            
            // Buscamos solo por nombre de usuario
            String sql = "SELECT * FROM usuarios WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPass = rs.getString("password");
                
                // Comparamos la clave ingresada con la de la base de datos
                if (BCrypt.checkpw(password, hashedPass)) {
                    usuario = mapearUsuario(rs);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al validar login: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }
        return usuario;
    }

    // Métodos de apoyo
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNombres(rs.getString("nombres"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setEmail(rs.getString("email"));
        usuario.setUsername(rs.getString("username"));
        usuario.setRol(rs.getString("rol"));
        return usuario;
    }

    private void cerrar(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            Conexion.closeConnection(conn);
        } catch (SQLException ex) {
            System.err.println("Error al cerrar: " + ex.getMessage());
        }
    }
}
