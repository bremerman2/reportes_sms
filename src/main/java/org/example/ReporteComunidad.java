package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReporteComunidad {
    private int idReporte;
    private LocalDateTime fechaHora;
    private String cuerpoSms;
    private String capturaPantalla;
    private String estado;
    private int idUsuario;
    private String nombreUsuario;
    private String numeroTelefono;
    private int cantidadReportesNumero;
    private List<ComentarioReporte> comentarios = new ArrayList<>();
    private boolean suscrito;

    public int getIdReporte() { return idReporte; }
    public void setIdReporte(int idReporte) { this.idReporte = idReporte; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getFechaTexto() {
        return fechaHora == null ? "" : fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    public String getCuerpoSms() { return cuerpoSms; }
    public void setCuerpoSms(String cuerpoSms) { this.cuerpoSms = cuerpoSms; }
    public String getCapturaPantalla() { return capturaPantalla; }
    public void setCapturaPantalla(String capturaPantalla) { this.capturaPantalla = capturaPantalla; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getNumeroTelefono() { return numeroTelefono; }
    public void setNumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }
    public int getCantidadReportesNumero() { return cantidadReportesNumero; }
    public void setCantidadReportesNumero(int cantidadReportesNumero) { this.cantidadReportesNumero = cantidadReportesNumero; }
    public int getCantidadComentarios() { return comentarios == null ? 0 : comentarios.size(); }
    public List<ComentarioReporte> getComentarios() { return comentarios; }
    public void setComentarios(List<ComentarioReporte> comentarios) { this.comentarios = comentarios; }
    public boolean isSuscrito() {return suscrito;}
    public void setSuscrito(boolean suscrito) {this.suscrito = suscrito;}
}
