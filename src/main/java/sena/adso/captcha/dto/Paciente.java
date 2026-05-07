package sena.adso.captcha.dto;

import java.util.Date;

/**
 * Clase que representa un paciente (persona vacunada)
 */
public class Paciente {
    private int id;
    private String nombres;
    private String apellidos;
    private String documento;
    private Date fechaNacimiento;
    
    public Paciente() {
    }
    
    public Paciente(int id, String nombres, String apellidos, String documento, Date fechaNacimiento) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    /**
     * Obtiene el nombre completo del paciente
     * @return Nombres y apellidos del paciente
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}
