package org.example;

import java.sql.Timestamp;

public class Suscripcion {

    private int id;
    private int id_usuario;
    private int id_reporte;
    private Timestamp fecha_suscripcion;

    public Suscripcion() {
    }

    public Suscripcion(int id_usuario, int id_reporte) {
        this.id_usuario = id_usuario;
        this.id_reporte = id_reporte;
    }

    public Suscripcion(int id, Integer id_usuario, int id_reporte, Timestamp fecha_suscripcion) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.id_reporte = id_reporte;
        this.fecha_suscripcion = fecha_suscripcion;
    }

    public Timestamp getFecha_suscripcion() {
        return fecha_suscripcion;
    }

    public int getId_reporte() {
        return id_reporte;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public void setId_reporte(int id_reporte) {
        this.id_reporte = id_reporte;
    }

    public void setFecha_suscripcion(Timestamp fecha_suscripcion) {
        this.fecha_suscripcion = fecha_suscripcion;
    }
}
