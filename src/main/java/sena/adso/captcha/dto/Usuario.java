package sena.adso.captcha.dto;

/**
 * Clase que representa un usuario del sistema (personal médico)
 */
public class Usuario {
    private int id;
    private String nombres;
    private String apellidos;
    private String documento;
    private String email;
    private String username;
    private String password;
    private String rol; // MEDICO o ENFERMERO
    private String especialidad;
    private String institucion;
    
    public Usuario() {
    }
    
    public Usuario(int id, String nombres, String apellidos, String documento, String email, 
                  String username, String password, String rol, String especialidad, String institucion) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.documento = documento;
        this.email = email;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.especialidad = especialidad;
        this.institucion = institucion;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }
    
    /**
     * Obtiene el nombre completo del usuario
     * @return Nombres y apellidos del usuario
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}
