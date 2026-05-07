package sena.adso.captcha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sena.adso.captcha.dto.Vacuna;
import sena.adso.captcha.model.Conexion;

/**
 * DAO para gestionar vacunas en el sistema
 */
public class VacunaDAO {
    
    /**
     * Inserta una nueva vacuna en la base de datos
     * @param vacuna Objeto Vacuna a insertar
     * @return true si la inserción fue exitosa
     */
    public boolean insertar(Vacuna vacuna) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getConnection();
            String sql = "INSERT INTO vacunas (nombre, lote, laboratorio, fecha_vencimiento) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, vacuna.getNombre());
            stmt.setString(2, vacuna.getLote());
            stmt.setString(3, vacuna.getLaboratorio());
            stmt.setString(4, vacuna.getFechaVencimiento());
            
            int filasAfectadas = stmt.executeUpdate();
            exito = filasAfectadas > 0;
            
        } catch (SQLException ex) {
            System.err.println("Error al insertar vacuna: " + ex.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
        
        return exito;
    }
    
    /**
     * Actualiza una vacuna existente
     * @param vacuna Objeto Vacuna con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    public boolean actualizar(Vacuna vacuna) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getConnection();
            String sql = "UPDATE vacunas SET nombre=?, lote=?, laboratorio=?, fecha_vencimiento=? WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, vacuna.getNombre());
            stmt.setString(2, vacuna.getLote());
            stmt.setString(3, vacuna.getLaboratorio());
            stmt.setString(4, vacuna.getFechaVencimiento());
            stmt.setInt(5, vacuna.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            exito = filasAfectadas > 0;
            
        } catch (SQLException ex) {
            System.err.println("Error al actualizar vacuna: " + ex.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
        
        return exito;
    }
    
    /**
     * Elimina una vacuna por su ID
     * @param id ID de la vacuna
     * @return true si la eliminación fue exitosa
     */
    public boolean eliminar(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getConnection();
            String sql = "DELETE FROM vacunas WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            exito = filasAfectadas > 0;
            
        } catch (SQLException ex) {
            System.err.println("Error al eliminar vacuna: " + ex.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
        
        return exito;
    }
    
    /**
     * Obtiene una vacuna por su ID
     * @param id ID de la vacuna
     * @return Objeto Vacuna o null si no se encuentra
     */
    public Vacuna obtenerPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vacuna vacuna = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM vacunas WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                vacuna = mapearVacuna(rs);
            }
            
        } catch (SQLException ex) {
            System.err.println("Error al obtener vacuna: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
        
        return vacuna;
    }
    
    /**
     * Obtiene todas las vacunas del sistema
     * @return Lista de objetos Vacuna
     */
    public List<Vacuna> obtenerTodas() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Vacuna> vacunas = new ArrayList<>();
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM vacunas ORDER BY nombre, lote";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Vacuna vacuna = mapearVacuna(rs);
                vacunas.add(vacuna);
            }
            
        } catch (SQLException ex) {
            System.err.println("Error al obtener vacunas: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
        
        return vacunas;
    }
    
    /**
     * Obtiene todas las vacunas por laboratorio
     * @param laboratorio Nombre del laboratorio
     * @return Lista de objetos Vacuna
     */
    public List<Vacuna> obtenerPorLaboratorio(String laboratorio) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Vacuna> vacunas = new ArrayList<>();
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM vacunas WHERE laboratorio=? ORDER BY nombre, lote";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, laboratorio);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Vacuna vacuna = mapearVacuna(rs);
                vacunas.add(vacuna);
            }
            
        } catch (SQLException ex) {
            System.err.println("Error al obtener vacunas por laboratorio: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
        
        return vacunas;
    }
    
    /**
     * Mapea un ResultSet a un objeto Vacuna
     * @param rs ResultSet con los datos de la vacuna
     * @return Objeto Vacuna con los datos
     * @throws SQLException si hay error en la manipulación del ResultSet
     */
    private Vacuna mapearVacuna(ResultSet rs) throws SQLException {
        Vacuna vacuna = new Vacuna();
        vacuna.setId(rs.getInt("id"));
        vacuna.setNombre(rs.getString("nombre"));
        vacuna.setLote(rs.getString("lote"));
        vacuna.setLaboratorio(rs.getString("laboratorio"));
        vacuna.setFechaVencimiento(rs.getString("fecha_vencimiento"));
        return vacuna;
    }
}
