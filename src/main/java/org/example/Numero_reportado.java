package org.example;

public class Numero_reportado {
    int id_numero_reportado;
    String numero_telefono;
    int cantidad_reportes;

    public Numero_reportado() {
    }

    public Numero_reportado(int id_numero_reportado, String numero_telefono, int cantidad_reportes) {
        this.id_numero_reportado = id_numero_reportado;
        this.numero_telefono = numero_telefono;
        this.cantidad_reportes = cantidad_reportes;
    }

    public int getId_numero_reportado() {
        return id_numero_reportado;
    }

    public void setId_numero_reportado(int id_numero_reportado) {
        this.id_numero_reportado = id_numero_reportado;
    }

    public String getNumero_telefono() {
        return numero_telefono;
    }

    public void setNumero_telefono(String numero_telefono) {
        this.numero_telefono = numero_telefono;
    }

    public int getCantidad_reportes() {
        return cantidad_reportes;
    }

    public void setCantidad_reportes(int cantidad_reportes) {
        this.cantidad_reportes = cantidad_reportes;
    }
}
