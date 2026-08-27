package org.example;

import java.time.LocalDateTime;

public class Reportes {
    int id_reporte;
    LocalDateTime fecha_hora;
    String cuerpo_sms;
    String captura_pantalla;
    boolean es_manual;
    estado_reporte estado_reporte;

    public Reportes() {
    }

    public Reportes(int id_reporte, LocalDateTime fecha_hora, String cuerpo_sms, String captura_pantalla, boolean es_manual, estado_reporte estado_reporte) {
        this.id_reporte = id_reporte;
        this.fecha_hora = fecha_hora;
        this.cuerpo_sms = cuerpo_sms;
        this.captura_pantalla = captura_pantalla;
        this.es_manual = es_manual;
        this.estado_reporte = estado_reporte;
    }

    public int getId_reporte() {
        return id_reporte;
    }

    public void setId_reporte(int id_reporte) {
        this.id_reporte = id_reporte;
    }

    public LocalDateTime getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(LocalDateTime fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getCuerpo_sms() {
        return cuerpo_sms;
    }

    public void setCuerpo_sms(String cuerpo_sms) {
        this.cuerpo_sms = cuerpo_sms;
    }

    public String getCaptura_pantalla() {
        return captura_pantalla;
    }

    public void setCaptura_pantalla(String captura_pantalla) {
        this.captura_pantalla = captura_pantalla;
    }

    public boolean isEs_manual() {
        return es_manual;
    }

    public void setEs_manual(boolean es_manual) {
        this.es_manual = es_manual;
    }

    public estado_reporte getEstado_reporte() {
        return estado_reporte;
    }

    public void setEstado_reporte(estado_reporte estado_reporte) {
        this.estado_reporte = estado_reporte;
    }
}
