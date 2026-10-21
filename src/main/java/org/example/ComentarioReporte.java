package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ComentarioReporte {
    private int idComentario;
    private int idReporte;
    private int idUsuario;
    private String nombreUsuario;
    private String comentario;
    private LocalDateTime fechaHora;

    public int getIdComentario() { return idComentario; }
    public void setIdComentario(int idComentario) { this.idComentario = idComentario; }
    public int getIdReporte() { return idReporte; }
    public void setIdReporte(int idReporte) { this.idReporte = idReporte; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getFechaTexto() {
        return fechaHora == null ? "" : fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
