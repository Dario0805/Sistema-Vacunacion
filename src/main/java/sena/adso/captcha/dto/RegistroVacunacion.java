package sena.adso.captcha.dto;

import java.util.Date;

/**
 * Clase que representa un registro de vacunación
 */
public class RegistroVacunacion {
    private int id;
    private int idPaciente;
    private String nombrePaciente; // Para facilitar la visualización
    private String documentoPaciente; // Para facilitar la visualización
    private int idVacuna;
    private String nombreVacuna; // Para facilitar la visualización
    private String loteVacuna; // Para facilitar la visualización
    private String laboratorio; // Para facilitar la visualización
    private Date fechaVacunacion;
    private int idPersonalMedico;
    private String nombrePersonalMedico; // Para facilitar la visualización
    private String lugarVacunacion;
    private String observaciones;
    
    public RegistroVacunacion() {
    }

    public RegistroVacunacion(int id, int idPaciente, String nombrePaciente, String documentoPaciente, 
                              int idVacuna, String nombreVacuna, String loteVacuna, String laboratorio, 
                              Date fechaVacunacion, int idPersonalMedico, String nombrePersonalMedico, 
                              String lugarVacunacion, String observaciones) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.nombrePaciente = nombrePaciente;
        this.documentoPaciente = documentoPaciente;
        this.idVacuna = idVacuna;
        this.nombreVacuna = nombreVacuna;
        this.loteVacuna = loteVacuna;
        this.laboratorio = laboratorio;
        this.fechaVacunacion = fechaVacunacion;
        this.idPersonalMedico = idPersonalMedico;
        this.nombrePersonalMedico = nombrePersonalMedico;
        this.lugarVacunacion = lugarVacunacion;
        this.observaciones = observaciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getDocumentoPaciente() {
        return documentoPaciente;
    }

    public void setDocumentoPaciente(String documentoPaciente) {
        this.documentoPaciente = documentoPaciente;
    }

    public int getIdVacuna() {
        return idVacuna;
    }

    public void setIdVacuna(int idVacuna) {
        this.idVacuna = idVacuna;
    }

    public String getNombreVacuna() {
        return nombreVacuna;
    }

    public void setNombreVacuna(String nombreVacuna) {
        this.nombreVacuna = nombreVacuna;
    }

    public String getLoteVacuna() {
        return loteVacuna;
    }

    public void setLoteVacuna(String loteVacuna) {
        this.loteVacuna = loteVacuna;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public Date getFechaVacunacion() {
        return fechaVacunacion;
    }

    public void setFechaVacunacion(Date fechaVacunacion) {
        this.fechaVacunacion = fechaVacunacion;
    }

    public int getIdPersonalMedico() {
        return idPersonalMedico;
    }

    public void setIdPersonalMedico(int idPersonalMedico) {
        this.idPersonalMedico = idPersonalMedico;
    }

    public String getNombrePersonalMedico() {
        return nombrePersonalMedico;
    }

    public void setNombrePersonalMedico(String nombrePersonalMedico) {
        this.nombrePersonalMedico = nombrePersonalMedico;
    }

    public String getLugarVacunacion() {
        return lugarVacunacion;
    }

    public void setLugarVacunacion(String lugarVacunacion) {
        this.lugarVacunacion = lugarVacunacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
