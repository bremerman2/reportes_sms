package org.example;

public class Administrador {
    int id_administrador;
    String email;
    String password;
    String nombre;
    String apellido;
    String telefono;

    public Administrador() {
    }

    public Administrador(int id_administrador, String email, String contrasenia, String nombre, String apellido, String telefono) {
        this.id_administrador = id_administrador;
        this.email = email;
        this.password = contrasenia;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    public int getId_administrador() {
        return id_administrador;
    }

    public void setId_administrador(int id_administrador) {
        this.id_administrador = id_administrador;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenia() {
        return password;
    }

    public void setContrasenia(String contrasenia) {
        this.password = contrasenia;
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
}