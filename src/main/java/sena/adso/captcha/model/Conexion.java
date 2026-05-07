package sena.adso.captcha.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Usamos las variables que configuramos en el panel de Render
    private static final String URL = System.getenv("SPRING_DATASOURCE_URL");
    private static final String USER = System.getenv("SPRING_DATASOURCE_USERNAME");
    private static final String PASS = System.getenv("SPRING_DATASOURCE_PASSWORD");

    public static Connection getConnection() throws SQLException {
        try {
            // CAMBIO: Ahora cargamos el driver de PostgreSQL
            Class.forName("org.postgresql.Driver");
            
            if (URL == null || URL.isEmpty()) {
                throw new SQLException("La variable SPRING_DATASOURCE_URL no está configurada en Render");
            }
            
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Error al cargar el driver de PostgreSQL. Revisa tu pom.xml", ex);
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar la conexión: " + ex.getMessage());
        }
    }
}
