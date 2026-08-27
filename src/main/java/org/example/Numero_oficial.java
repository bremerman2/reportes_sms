package org.example;

import java.time.LocalDate;

public class Numero_oficial {
    int id_numero_oficial;
    String numero_telefono;
    String nombre_entidad;
    LocalDate fecha_carga;
    Pais_prefijo pais_prefijo;
    Administrador administrador;

    public Numero_oficial() {
    }

    public Numero_oficial(int id_numero_oficial, String numero_telefono, String nombre_entidad, LocalDate fecha_carga, Pais_prefijo pais_prefijo, Administrador administrador) {
        this.id_numero_oficial = id_numero_oficial;
        this.numero_telefono = numero_telefono;
        this.nombre_entidad = nombre_entidad;
        this.fecha_carga = fecha_carga;
        this.pais_prefijo = pais_prefijo;
        this.administrador = administrador;
    }

    public int getId_numero_oficial() {
        return id_numero_oficial;
    }

    public void setId_numero_oficial(int id_numero_oficial) {
        this.id_numero_oficial = id_numero_oficial;
    }

    public String getNumero_telefono() {
        return numero_telefono;
    }

    public void setNumero_telefono(String numero_telefono) {
        this.numero_telefono = numero_telefono;
    }

    public String getNombre_entidad() {
        return nombre_entidad;
    }

    public void setNombre_entidad(String nombre_entidad) {
        this.nombre_entidad = nombre_entidad;
    }

    public LocalDate getFecha_carga() {
        return fecha_carga;
    }

    public void setFecha_carga(LocalDate fecha_carga) {
        this.fecha_carga = fecha_carga;
    }

    public Pais_prefijo getPais_prefijo() {
        return pais_prefijo;
    }

    public void setPais_prefijo(Pais_prefijo pais_prefijo) {
        this.pais_prefijo = pais_prefijo;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }
}
