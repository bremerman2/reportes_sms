package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // Fecha separada del horario con un punto: "29/10/2026 · 16:50"
    public String getFechaLarga() {
        return fechaHora == null ? "" : fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy '·' HH:mm"));
    }

    // ---------- Enlaces dentro del SMS (para resaltarlos y avisar en el detalle del reporte) ----------

    private static final Pattern ENLACE = Pattern.compile(
            "(?i)\\b((?:https?://|www\\.)[^\\s<>\"]+|[a-z0-9-]+(?:\\.[a-z0-9-]+)*\\.(?:com|net|org|info|xyz|top|site|online|link|click|uy|ar|io|co|me|ly|app)(?:/[^\\s<>\"]*)?)");

    // El SMS partido en trozos de texto común y trozos que son enlaces, en orden
    public List<ParteSms> getPartesSms() {
        List<ParteSms> partes = new ArrayList<>();
        if (cuerpoSms == null) return partes;
        Matcher m = ENLACE.matcher(cuerpoSms);
        int desde = 0;
        while (m.find()) {
            // el punto o la coma final suele ser parte de la oración, no del enlace
            int fin = m.end();
            while (fin > m.start() && ".,;:!?)".indexOf(cuerpoSms.charAt(fin - 1)) >= 0) fin--;
            if (m.start() > desde) partes.add(new ParteSms(cuerpoSms.substring(desde, m.start()), false));
            partes.add(new ParteSms(cuerpoSms.substring(m.start(), fin), true));
            desde = fin;
        }
        if (desde < cuerpoSms.length()) partes.add(new ParteSms(cuerpoSms.substring(desde), false));
        return partes;
    }

    public boolean isTieneEnlace() {
        return cuerpoSms != null && ENLACE.matcher(cuerpoSms).find();
    }

    public static class ParteSms {
        private final String texto;
        private final boolean enlace;

        public ParteSms(String texto, boolean enlace) {
            this.texto = texto;
            this.enlace = enlace;
        }

        public String getTexto() { return texto; }
        public boolean isEnlace() { return enlace; }
    }
}
