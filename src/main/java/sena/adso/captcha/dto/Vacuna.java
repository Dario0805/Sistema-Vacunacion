package sena.adso.captcha.dto;

/**
 * Clase que representa una vacuna en el sistema
 */
public class Vacuna {
    private int id;
    private String nombre;
    private String lote;
    private String laboratorio;
    private String fechaVencimiento;
    
    public Vacuna() {
    }
    
    public Vacuna(int id, String nombre, String lote, String laboratorio, String fechaVencimiento) {
        this.id = id;
        this.nombre = nombre;
        this.lote = lote;
        this.laboratorio = laboratorio;
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    /**
     * Obtiene información completa de la vacuna
     * @return String con todos los datos de la vacuna
     */
    public String getInfoCompleta() {
        return nombre + " (Lote: " + lote + ", Lab: " + laboratorio + ")";
    }
}
