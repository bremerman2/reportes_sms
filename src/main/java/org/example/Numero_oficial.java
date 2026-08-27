package org.example;

import java.time.LocalDate;

public class Numero_oficial {
    int id_numero_oficial;
    String numero_telefono;
    LocalDate fecha_carga;

    public Numero_oficial() {
    }

    public Numero_oficial(int id_numero_oficial, String numero_telefono, LocalDate fecha_carga) {
        this.id_numero_oficial = id_numero_oficial;
        this.numero_telefono = numero_telefono;
        this.fecha_carga = fecha_carga;
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

    public LocalDate getFecha_carga() {
        return fecha_carga;
    }

    public void setFecha_carga(LocalDate fecha_carga) {
        this.fecha_carga = fecha_carga;
    }
}
