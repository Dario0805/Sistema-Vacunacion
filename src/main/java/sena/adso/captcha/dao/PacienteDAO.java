package sena.adso.captcha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import sena.adso.captcha.dto.Paciente;
import sena.adso.captcha.model.Conexion;

/**
 * DAO para gestionar pacientes en el sistema
 */
public class PacienteDAO {
    
    /**
     * Inserta un nuevo paciente en la base de datos
     * @param paciente Objeto Paciente a insertar
     * @return true si la inserción fue exitosa
     */
    public boolean insertar(Paciente paciente) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getConnection();
            String sql = "INSERT INTO pacientes (nombres, apellidos, documento, fecha_nacimiento) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, paciente.getNombres());
            stmt.setString(2, paciente.getApellidos());
            stmt.setString(3, paciente.getDocumento());
            stmt.setDate(4, new Date(paciente.getFechaNacimiento().getTime()));
            
            int filasAfectadas = stmt.executeUpdate();
            exito = filasAfectadas > 0;
            
        } catch (SQLException ex) {
            System.err.println("Error al insertar paciente: " + ex.getMessage());
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
     * Actualiza un paciente existente
     * @param paciente Objeto Paciente con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    public boolean actualizar(Paciente paciente) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getConnection();
            String sql = "UPDATE pacientes SET nombres=?, apellidos=?, documento=?, fecha_nacimiento=? WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, paciente.getNombres());
            stmt.setString(2, paciente.getApellidos());
            stmt.setString(3, paciente.getDocumento());
            stmt.setDate(4, new Date(paciente.getFechaNacimiento().getTime()));
            stmt.setInt(5, paciente.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            exito = filasAfectadas > 0;
            
        } catch (SQLException ex) {
            System.err.println("Error al actualizar paciente: " + ex.getMessage());
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
     * Elimina un paciente por su ID
     * @param id ID del paciente
     * @return true si la eliminación fue exitosa
     */
    public boolean eliminar(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getConnection();
            String sql = "DELETE FROM pacientes WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            exito = filasAfectadas > 0;
            
        } catch (SQLException ex) {
            System.err.println("Error al eliminar paciente: " + ex.getMessage());
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
     * Obtiene un paciente por su ID
     * @param id ID del paciente
     * @return Objeto Paciente o null si no se encuentra
     */
    public Paciente obtenerPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Paciente paciente = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM pacientes WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                paciente = mapearPaciente(rs);
            }
            
        } catch (SQLException ex) {
            System.err.println("Error al obtener paciente: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
        
        return paciente;
    }
    
    /**
     * Obtiene un paciente por su número de documento
     * @param documento Número de documento del paciente
     * @return Objeto Paciente o null si no se encuentra
     */
    public Paciente obtenerPorDocumento(String documento) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Paciente paciente = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM pacientes WHERE documento=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, documento);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                paciente = mapearPaciente(rs);
            }
            
        } catch (SQLException ex) {
            System.err.println("Error al obtener paciente por documento: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
        
        return paciente;
    }
    
    /**
     * Obtiene todos los pacientes del sistema
     * @return Lista de objetos Paciente
     */
    public List<Paciente> obtenerTodos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Paciente> pacientes = new ArrayList<>();
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM pacientes ORDER BY apellidos, nombres";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Paciente paciente = mapearPaciente(rs);
                pacientes.add(paciente);
            }
            
        } catch (SQLException ex) {
            System.err.println("Error al obtener pacientes: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
        
        return pacientes;
    }
    
    /**
     * Mapea un ResultSet a un objeto Paciente
     * @param rs ResultSet con los datos del paciente
     * @return Objeto Paciente con los datos
     * @throws SQLException si hay error en la manipulación del ResultSet
     */
    private Paciente mapearPaciente(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setId(rs.getInt("id"));
        paciente.setNombres(rs.getString("nombres"));
        paciente.setApellidos(rs.getString("apellidos"));
        paciente.setDocumento(rs.getString("documento"));
        paciente.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        return paciente;
    }
}
