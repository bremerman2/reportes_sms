package org.example;

public class Pais_prefijo {
    int id_prefijo;
    String prefijo;
    String nombre_pais;

    public Pais_prefijo() {
    }

    public Pais_prefijo(int id_prefijo, String prefijo, String nombre_pais) {
        this.id_prefijo = id_prefijo;
        this.prefijo = prefijo;
        this.nombre_pais = nombre_pais;
    }

    public int getId_prefijo() {
        return id_prefijo;
    }

    public void setId_prefijo(int id_prefijo) {
        this.id_prefijo = id_prefijo;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public String getNombre_pais() {
        return nombre_pais;
    }

    public void setNombre_pais(String nombre_pais) {
        this.nombre_pais = nombre_pais;
    }
}
