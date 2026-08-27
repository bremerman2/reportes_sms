package org.example;

public class Usuario {
    int id_usuario;
    boolean permiso_sms;
    String token_dispositivo;
    String nombre;
    String apellido;
    String telefono;
    String email;
    String password;
    Administrador administrador;

    public Usuario() {
    }

    public Usuario(int id_usuario, boolean permiso_sms, String token_dispositivo, String nombre, String apellido, String telefono, String email, String password, Administrador administrador){
        this.id_usuario = id_usuario;
        this.permiso_sms = permiso_sms;
        this.token_dispositivo = token_dispositivo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.administrador = administrador;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public boolean isPermiso_sms() {
        return permiso_sms;
    }

    public void setPermiso_sms(boolean permiso_sms) {
        this.permiso_sms = permiso_sms;
    }

    public String getToken_dispositivo() {
        return token_dispositivo;
    }

    public void setToken_dispositivo(String token_dispositivo) {
        this.token_dispositivo = token_dispositivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }
}
