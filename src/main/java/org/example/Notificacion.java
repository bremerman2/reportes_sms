package org.example;

import java.sql.Timestamp;

public class Notificacion {
    private int id;
    private int id_usuario;
    private String titulo;
    private String mensaje;
    private boolean leido;
    private String enlace;
    private Timestamp fecha_creacion;

    public Notificacion() {
    }

    public Notificacion(Integer id_usuario, String titulo, String mensaje, String enlace) {
        this.id_usuario = id_usuario;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.enlace = enlace;
        this.leido = false;
    }

    public int getId() {
        return id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean isLeido() {
        return leido;
    }

    public String getEnlace() {
        return enlace;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}