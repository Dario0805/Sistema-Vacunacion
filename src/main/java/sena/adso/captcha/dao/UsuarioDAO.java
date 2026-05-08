package sena.adso.captcha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sena.adso.captcha.dto.Usuario;
import sena.adso.captcha.model.Conexion;
// IMPORTANTE: Asegúrate de tener la librería jbcrypt en tu pom.xml
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {

    // ... (Mantén los métodos insertar, actualizar, eliminar e obtenerPorId igual) ...

    public Usuario validarLogin(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            username = (username != null) ? username.trim() : "";
            password = (password != null) ? password.trim() : "";

            System.out.println("Intentando login para: [" + username + "]");

            conn = Conexion.getConnection();
            
            // BUSCAMOS SOLO POR USUARIO (No por password en el SQL)
            String sql = "SELECT * FROM usuarios WHERE username = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String passwordEnBD = rs.getString("password");

                // COMPARACIÓN DE SEGURIDAD:
                // Comprobamos si la clave escrita coincide con la encriptada ($2a$10...)
                if (BCrypt.checkpw(password, passwordEnBD)) {
                    System.out.println("Login exitoso: Contraseña correcta");
                    usuario = mapearUsuario(rs);
                } else {
                    System.out.println("Login fallido: Contraseña incorrecta");
                }
            } else {
                System.out.println("Login fallido: Usuario no existe");
            }

        } catch (SQLException ex) {
            System.err.println("Error al validar login: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }

        return usuario;
    }

    // ... (Mantén los métodos obtenerTodos, mapearUsuario y cerrar igual) ...
}
