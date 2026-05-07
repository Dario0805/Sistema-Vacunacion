package sena.adso.captcha.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Se obtienen de las variables de entorno de Render; si no existen, usa los valores por defecto (localhost)
    private static final String HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String PORT = System.getenv().getOrDefault("DB_PORT", "1433");
    private static final String DBNAME = System.getenv().getOrDefault("DB_NAME", "vacunacion_db");
    
    // URL dinámica para soportar Ngrok y SSL obligatorio en la nube
    private static final String URL = "jdbc:sqlserver://" + HOST + ":" + PORT + 
                                     ";databaseName=" + DBNAME + 
                                     ";encrypt=true;trustServerCertificate=true;";

    private static final String USER = System.getenv().getOrDefault("DB_USER", "sa"); 
    private static final String PASS = System.getenv().getOrDefault("DB_PASS", "123");

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Error al cargar el driver de SQL Server", ex);
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