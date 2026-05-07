package sena.adso.captcha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import sena.adso.captcha.dto.RegistroVacunacion;
import sena.adso.captcha.model.Conexion;

/**
 * DAO para gestionar registros de vacunación
 */
public class RegistroVacunacionDAO {
    
    /**
     * Inserta un nuevo registro de vacunación
     * @param registro Objeto RegistroVacunacion a insertar
     * @return true si la inserción fue exitosa
     */
    public boolean insertar(RegistroVacunacion registro) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getConnection();
            String sql = "INSERT INTO registros_vacunacion (id_paciente, id_vacuna, fecha_vacunacion, id_personal_medico, "
                    + "lugar_vacunacion, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, registro.getIdPaciente());
            stmt.setInt(2, registro.getIdVacuna());
            stmt.setTimestamp(3, new Timestamp(registro.getFechaVacunacion().getTime()));
            stmt.setInt(4, registro.getIdPersonalMedico());
            stmt.setString(5, registro.getLugarVacunacion());
            stmt.setString(6, registro.getObservaciones());
            
            int filasAfectadas = stmt.executeUpdate();
            exito = filasAfectadas > 0;
            
            // Registrar log del cambio
            if(exito) {
                registrarLog("INSERT", registro.getId(), registro.getIdPersonalMedico());
            }
            
        } catch (SQLException ex) {
            System.err.println("Error al insertar registro de vacunación: " + ex.getMessage());
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
     * Actualiza un registro de vacunación existente
     * @param registro Objeto RegistroVacunacion con los datos actualizados
     * @return true si la actualización fue exitosa
     */
    public boolean actualizar(RegistroVacunacion registro) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getConnection();
            String sql = "UPDATE registros_vacunacion SET id_paciente=?, id_vacuna=?, fecha_vacunacion=?, "
                    + "id_personal_medico=?, lugar_vacunacion=?, observaciones=? WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, registro.getIdPaciente());
            stmt.setInt(2, registro.getIdVacuna());
            stmt.setTimestamp(3, new Timestamp(registro.getFechaVacunacion().getTime()));
            stmt.setInt(4, registro.getIdPersonalMedico());
            stmt.setString(5, registro.getLugarVacunacion());
            stmt.setString(6, registro.getObservaciones());
            stmt.setInt(7, registro.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            exito = filasAfectadas > 0;
            
            // Registrar log del cambio
            if(exito) {
                registrarLog("UPDATE", registro.getId(), registro.getIdPersonalMedico());
            }
            
        } catch (SQLException ex) {
            System.err.println("Error al actualizar registro de vacunación: " + ex.getMessage());
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
     * Elimina un registro de vacunación por su ID
     * @param id ID del registro
     * @param idUsuario ID del usuario que realiza la eliminación (para el log)
     * @return true si la eliminación fue exitosa
     */
    public boolean eliminar(int id, int idUsuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getConnection();
            String sql = "DELETE FROM registros_vacunacion WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            exito = filasAfectadas > 0;
            
            // Registrar log del cambio
            if(exito) {
                registrarLog("DELETE", id, idUsuario);
            }
            
        } catch (SQLException ex) {
            System.err.println("Error al eliminar registro de vacunación: " + ex.getMessage());
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
     * Obtiene un registro de vacunación por su ID
     * @param id ID del registro
     * @return Objeto RegistroVacunacion o null si no se encuentra
     */
    public RegistroVacunacion obtenerPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        RegistroVacunacion registro = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT r.*, p.nombres as nombre_paciente, p.apellidos as apellido_paciente, " +
                       "p.documento as documento_paciente, v.nombre as nombre_vacuna, v.lote as lote_vacuna, " +
                       "v.laboratorio, CONCAT(u.nombres, ' ', u.apellidos) as nombre_personal_medico " +
                       "FROM registros_vacunacion r " +
                       "JOIN pacientes p ON r.id_paciente = p.id " +
                       "JOIN vacunas v ON r.id_vacuna = v.id " +
                       "JOIN usuarios u ON r.id_personal_medico = u.id " +
                       "WHERE r.id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                registro = mapearRegistro(rs);
            }
            
        } catch (SQLException ex) {
            System.err.println("Error al obtener registro de vacunación: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
        
        return registro;
    }
    
    /**
     * Obtiene todos los registros de vacunación
     * @return Lista de objetos RegistroVacunacion
     */
    public List<RegistroVacunacion> obtenerTodos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RegistroVacunacion> registros = new ArrayList<>();
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT r.*, p.nombres as nombre_paciente, p.apellidos as apellido_paciente, " +
                       "p.documento as documento_paciente, v.nombre as nombre_vacuna, v.lote as lote_vacuna, " +
                       "v.laboratorio, CONCAT(u.nombres, ' ', u.apellidos) as nombre_personal_medico " +
                       "FROM registros_vacunacion r " +
                       "JOIN pacientes p ON r.id_paciente = p.id " +
                       "JOIN vacunas v ON r.id_vacuna = v.id " +
                       "JOIN usuarios u ON r.id_personal_medico = u.id " +
                       "ORDER BY r.fecha_vacunacion DESC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                RegistroVacunacion registro = mapearRegistro(rs);
                registros.add(registro);
            }
            
        } catch (SQLException ex) {
            System.err.println("Error al obtener registros de vacunación: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
        
        return registros;
    }
    
    /**
     * Obtiene todos los registros de vacunación de un paciente por su documento
     * @param documento Documento del paciente
     * @return Lista de objetos RegistroVacunacion
     */
    public List<RegistroVacunacion> obtenerPorDocumentoPaciente(String documento) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RegistroVacunacion> registros = new ArrayList<>();
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT r.*, p.nombres as nombre_paciente, p.apellidos as apellido_paciente, " +
                       "p.documento as documento_paciente, v.nombre as nombre_vacuna, v.lote as lote_vacuna, " +
                       "v.laboratorio, CONCAT(u.nombres, ' ', u.apellidos) as nombre_personal_medico " +
                       "FROM registros_vacunacion r " +
                       "JOIN pacientes p ON r.id_paciente = p.id " +
                       "JOIN vacunas v ON r.id_vacuna = v.id " +
                       "JOIN usuarios u ON r.id_personal_medico = u.id " +
                       "WHERE p.documento = ? " +
                       "ORDER BY r.fecha_vacunacion DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, documento);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                RegistroVacunacion registro = mapearRegistro(rs);
                registros.add(registro);
            }
            
        } catch (SQLException ex) {
            System.err.println("Error al obtener registros de vacunación por documento: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
        
        return registros;
    }
    
    /**
     * Obtiene todos los registros de vacunación de un paciente por su ID
     * @param idPaciente ID del paciente
     * @return Lista de objetos RegistroVacunacion
     */
    public List<RegistroVacunacion> obtenerPorIdPaciente(int idPaciente) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RegistroVacunacion> registros = new ArrayList<>();
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT r.*, p.nombres as nombre_paciente, p.apellidos as apellido_paciente, " +
                       "p.documento as documento_paciente, v.nombre as nombre_vacuna, v.lote as lote_vacuna, " +
                       "v.laboratorio, CONCAT(u.nombres, ' ', u.apellidos) as nombre_personal_medico " +
                       "FROM registros_vacunacion r " +
                       "JOIN pacientes p ON r.id_paciente = p.id " +
                       "JOIN vacunas v ON r.id_vacuna = v.id " +
                       "JOIN usuarios u ON r.id_personal_medico = u.id " +
                       "WHERE r.id_paciente = ? " +
                       "ORDER BY r.fecha_vacunacion DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idPaciente);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                RegistroVacunacion registro = mapearRegistro(rs);
                registros.add(registro);
            }
            
        } catch (SQLException ex) {
            System.err.println("Error al obtener registros de vacunación por ID de paciente: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
        
        return registros;
    }
    
    /**
     * Registra un log de cambios en la tabla de logs
     * @param accion Tipo de acción realizada (INSERT, UPDATE, DELETE)
     * @param idRegistro ID del registro modificado
     * @param idUsuario ID del usuario que realizó la modificación
     */
    private void registrarLog(String accion, int idRegistro, int idUsuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "INSERT INTO log_cambios (accion, id_registro, id_usuario, fecha) VALUES (?, ?, ?, GETDATE())";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, accion);
            stmt.setInt(2, idRegistro);
            stmt.setInt(3, idUsuario);
            stmt.executeUpdate();
            
        } catch (SQLException ex) {
            System.err.println("Error al registrar log: " + ex.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                Conexion.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto RegistroVacunacion
     * @param rs ResultSet con los datos del registro
     * @return Objeto RegistroVacunacion con los datos
     * @throws SQLException si hay error en la manipulación del ResultSet
     */
    private RegistroVacunacion mapearRegistro(ResultSet rs) throws SQLException {
        RegistroVacunacion registro = new RegistroVacunacion();
        registro.setId(rs.getInt("id"));
        registro.setIdPaciente(rs.getInt("id_paciente"));
        registro.setNombrePaciente(rs.getString("nombre_paciente") + " " + rs.getString("apellido_paciente"));
        registro.setDocumentoPaciente(rs.getString("documento_paciente"));
        registro.setIdVacuna(rs.getInt("id_vacuna"));
        registro.setNombreVacuna(rs.getString("nombre_vacuna"));
        registro.setLoteVacuna(rs.getString("lote_vacuna"));
        registro.setLaboratorio(rs.getString("laboratorio"));
        registro.setFechaVacunacion(rs.getTimestamp("fecha_vacunacion"));
        registro.setIdPersonalMedico(rs.getInt("id_personal_medico"));
        registro.setNombrePersonalMedico(rs.getString("nombre_personal_medico"));
        registro.setLugarVacunacion(rs.getString("lugar_vacunacion"));
        registro.setObservaciones(rs.getString("observaciones"));
        return registro;
    }
}
