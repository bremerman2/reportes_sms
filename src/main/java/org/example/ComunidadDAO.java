package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComunidadDAO {

    public List<ReporteComunidad> listarReportesComunidad() {
        List<ReporteComunidad> lista = new ArrayList<>();
        String sql = "SELECT r.id_reporte, r.fecha_hora, r.cuerpo_sms, r.captura_pantalla, " +
                "r.estado_reporte, r.id_usuario, u.nombre, u.apellido, nr.numero_telefono, nr.cantidad_reportes " +
                "FROM reporte r JOIN usuario u ON u.id_usuario = r.id_usuario " +
                "JOIN numero_reportado nr ON nr.id_num_reportado = r.id_num_reportado " +
                "ORDER BY r.fecha_hora DESC, r.id_reporte DESC";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ReporteComunidad r = new ReporteComunidad();
                r.setIdReporte(rs.getInt("id_reporte"));
                Timestamp fecha = rs.getTimestamp("fecha_hora");
                r.setFechaHora(fecha == null ? null : fecha.toLocalDateTime());
                r.setCuerpoSms(rs.getString("cuerpo_sms"));
                r.setCapturaPantalla(rs.getString("captura_pantalla"));
                r.setEstado(rs.getString("estado_reporte"));
                r.setIdUsuario(rs.getInt("id_usuario"));
                r.setNombreUsuario(rs.getString("nombre") + " " + rs.getString("apellido"));
                r.setNumeroTelefono(rs.getString("numero_telefono"));
                r.setCantidadReportesNumero(rs.getInt("cantidad_reportes"));
                r.setComentarios(listarComentarios(rs.getInt("id_reporte")));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar el muro de reportes: " + e.getMessage());
        }
        return lista;
    }

    private List<ComentarioReporte> listarComentarios(int idReporte) throws SQLException {
        List<ComentarioReporte> lista = new ArrayList<>();
        String sql = "SELECT c.id_comentario, c.id_reporte, c.id_usuario, c.comentario, c.fecha_hora, " +
                "u.nombre, u.apellido FROM comentario_reporte c JOIN usuario u ON u.id_usuario = c.id_usuario " +
                "WHERE c.id_reporte = ? ORDER BY c.fecha_hora ASC, c.id_comentario ASC";
        try (Connection con = ConexionDB.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReporte);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ComentarioReporte c = new ComentarioReporte();
                    c.setIdComentario(rs.getInt("id_comentario"));
                    c.setIdReporte(rs.getInt("id_reporte"));
                    c.setIdUsuario(rs.getInt("id_usuario"));
                    c.setComentario(rs.getString("comentario"));
                    Timestamp fecha = rs.getTimestamp("fecha_hora");
                    c.setFechaHora(fecha == null ? null : fecha.toLocalDateTime());
                    c.setNombreUsuario(rs.getString("nombre") + " " + rs.getString("apellido"));
                    lista.add(c);
                }
            }
        }
        return lista;
    }

    public boolean registrarReporte(int idUsuario, String telefono, int idPrefijo, String cuerpoSms, String captura) {
        String buscarNumero = "SELECT id_num_reportado FROM numero_reportado WHERE numero_telefono = ?";
        String insertarNumero = "INSERT INTO numero_reportado (numero_telefono, cantidad_reportes, id_prefijo) VALUES (?, 0, ?)";
        String insertarReporte = "INSERT INTO reporte (fecha_hora, cuerpo_sms, captura_pantalla, es_manual, estado_reporte, id_usuario, id_num_reportado) VALUES (?, ?, ?, FALSE, 'pendiente', ?, ?)";
        String incrementar = "UPDATE numero_reportado SET cantidad_reportes = cantidad_reportes + 1 WHERE id_num_reportado = ?";

        try (Connection con = ConexionDB.obtenerConexion()) {
            con.setAutoCommit(false);
            try {
                int idNumero;
                try (PreparedStatement ps = con.prepareStatement(buscarNumero)) {
                    ps.setString(1, telefono);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            idNumero = rs.getInt("id_num_reportado");
                        } else {
                            try (PreparedStatement ins = con.prepareStatement(insertarNumero, Statement.RETURN_GENERATED_KEYS)) {
                                ins.setString(1, telefono);
                                ins.setInt(2, idPrefijo);
                                ins.executeUpdate();
                                try (ResultSet keys = ins.getGeneratedKeys()) {
                                    if (!keys.next()) throw new SQLException("No se generó el ID del número reportado.");
                                    idNumero = keys.getInt(1);
                                }
                            }
                        }
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(insertarReporte)) {
                    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(2, cuerpoSms);
                    if (captura == null || captura.isBlank()) ps.setNull(3, Types.VARCHAR); else ps.setString(3, captura);
                    ps.setInt(4, idUsuario);
                    ps.setInt(5, idNumero);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(incrementar)) {
                    ps.setInt(1, idNumero);
                    ps.executeUpdate();
                }
                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                System.err.println("Error SQL al crear reporte comunitario: " + e.getMessage());
                return false;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión al crear reporte comunitario: " + e.getMessage());
            return false;
        }
    }

    public boolean agregarComentario(int idReporte, int idUsuario, String comentario) {
        String existe = "SELECT id_reporte FROM reporte WHERE id_reporte = ?";
        String insert = "INSERT INTO comentario_reporte (id_reporte, id_usuario, comentario, fecha_hora) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion()) {
            try (PreparedStatement ps = con.prepareStatement(existe)) {
                ps.setInt(1, idReporte);
                try (ResultSet rs = ps.executeQuery()) { if (!rs.next()) return false; }
            }
            try (PreparedStatement ps = con.prepareStatement(insert)) {
                ps.setInt(1, idReporte);
                ps.setInt(2, idUsuario);
                ps.setString(3, comentario);
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                return ps.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            System.err.println("Error al agregar comentario: " + e.getMessage());
            return false;
        }
    }
}
