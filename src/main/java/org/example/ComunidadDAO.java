package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComunidadDAO {

    // Sin búsqueda: trae todos los reportes
    public List<ReporteComunidad> listarReportesComunidad() {
        return listarReportesComunidad("");
    }

    // Con búsqueda: filtra por texto del SMS o por número (con o sin prefijo, con o sin espacios)
    public List<ReporteComunidad> listarReportesComunidad(String busqueda) {
        List<ReporteComunidad> lista = new ArrayList<>();
        String texto = busqueda == null ? "" : busqueda.trim();
        String digitos = soloDigitos(texto);
        if (digitos.length() < 3) digitos = "";   // con menos de 3 dígitos coincidiría con casi todos los números

        StringBuilder sql = new StringBuilder(
                "SELECT r.id_reporte, r.fecha_hora, r.cuerpo_sms, r.captura_pantalla, " +
                        "r.estado_reporte, r.id_usuario, u.nombre, u.apellido, nr.numero_telefono, nr.cantidad_reportes, pp.prefijo " +
                        "FROM reporte r JOIN usuario u ON u.id_usuario = r.id_usuario " +
                        "JOIN numero_reportado nr ON nr.id_num_reportado = r.id_num_reportado " +
                        "LEFT JOIN pais_prefijo pp ON pp.id_prefijo = nr.id_prefijo");
        if (!texto.isEmpty()) {
            sql.append(" WHERE LOWER(r.cuerpo_sms) LIKE LOWER(?)");
            if (!digitos.isEmpty()) {
                sql.append(" OR REGEXP_REPLACE(CONCAT(COALESCE(pp.prefijo, ''), nr.numero_telefono), '[^0-9]', '') LIKE ?");
            }
        }
        sql.append(" ORDER BY r.fecha_hora DESC, r.id_reporte DESC");

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            if (!texto.isEmpty()) {
                ps.setString(1, "%" + escaparLike(texto) + "%");
                if (!digitos.isEmpty()) ps.setString(2, "%" + digitos + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
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
                    r.setNumeroTelefono(numeroConPrefijo(rs.getString("prefijo"), rs.getString("numero_telefono")));
                    r.setCantidadReportesNumero(rs.getInt("cantidad_reportes"));
                    r.setComentarios(listarComentarios(rs.getInt("id_reporte")));
                    lista.add(r);
                }
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

    public int registrarReporte(int idUsuario, String telefono, int idPrefijo, String cuerpoSms, String captura) {
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
                int idReporteGenerado = -1;
                try (PreparedStatement ps = con.prepareStatement(insertarReporte, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(2, cuerpoSms);
                    if (captura == null || captura.isBlank()) ps.setNull(3, Types.VARCHAR); else ps.setString(3, captura);
                    ps.setInt(4, idUsuario);
                    ps.setInt(5, idNumero);
                    ps.executeUpdate();

                    //es necesario obtener el id del reporte recien generado para poder añadirlo a la suscripcion automatica
                    try (ResultSet keysReporte = ps.getGeneratedKeys()) {
                        if (!keysReporte.next()) throw new SQLException("No se generó el ID del reporte.");
                        idReporteGenerado = keysReporte.getInt(1);
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(incrementar)) {
                    ps.setInt(1, idNumero);
                    ps.executeUpdate();
                }
                con.commit();
                return idReporteGenerado;//se devuelve el id en lugar de un bool

            } catch (SQLException e) {
                con.rollback();
                System.err.println("Error SQL al crear reporte comunitario: " + e.getMessage());
                return -1;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión al crear reporte comunitario: " + e.getMessage());
            return -1;
        }
    }

    // ---------- Inicio del usuario: reportes recientes con búsqueda, filtro y paginación ----------

    private static final String FROM_RECIENTES =
            " FROM reporte r" +
            " JOIN numero_reportado nr ON nr.id_num_reportado = r.id_num_reportado" +
            " LEFT JOIN pais_prefijo pp ON pp.id_prefijo = nr.id_prefijo";

    // Arma el WHERE según los filtros que vinieron. digitos = solo los números de la búsqueda.
    private String whereRecientes(String digitos, String estado) {
        StringBuilder where = new StringBuilder(" WHERE 1 = 1");
        if (!digitos.isEmpty()) {
            // Se compara sin espacios, guiones ni "+" (prefijo + número), así "91 204" encuentra "+598 91 204 773"
            where.append(" AND REGEXP_REPLACE(CONCAT(COALESCE(pp.prefijo, ''), nr.numero_telefono), '[^0-9]', '') LIKE ?");
        }
        if (!estado.isEmpty()) {
            where.append(" AND r.estado_reporte = ?");
        }
        return where.toString();
    }

    // Carga los parámetros del WHERE y devuelve el índice del próximo "?"
    private int cargarFiltros(PreparedStatement ps, String digitos, String estado) throws SQLException {
        int i = 1;
        if (!digitos.isEmpty()) ps.setString(i++, "%" + digitos + "%");
        if (!estado.isEmpty()) ps.setString(i++, estado);
        return i;
    }

    // Para que un % o _ escrito por el usuario se busque literal y no como comodín del LIKE
    private String escaparLike(String texto) {
        return texto.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }

    private String soloDigitos(String numero) {
        return numero == null ? "" : numero.replaceAll("\\D", "");
    }

    public int contarRecientes(String numero, String estado) {
        String digitos = soloDigitos(numero);
        String sql = "SELECT COUNT(*)" + FROM_RECIENTES + whereRecientes(digitos, estado);
        try (Connection con = ConexionDB.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            cargarFiltros(ps, digitos, estado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar reportes recientes: " + e.getMessage());
        }
        return 0;
    }

    public List<ReporteComunidad> listarRecientes(String numero, String estado, int desde, int cantidad) {
        List<ReporteComunidad> lista = new ArrayList<>();
        String digitos = soloDigitos(numero);
        String sql = "SELECT r.id_reporte, r.fecha_hora, r.estado_reporte, nr.numero_telefono, pp.prefijo" +
                FROM_RECIENTES + whereRecientes(digitos, estado) +
                " ORDER BY r.fecha_hora DESC, r.id_reporte DESC LIMIT ? OFFSET ?";
        try (Connection con = ConexionDB.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            int i = cargarFiltros(ps, digitos, estado);
            ps.setInt(i++, cantidad);
            ps.setInt(i, desde);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteComunidad r = new ReporteComunidad();
                    r.setIdReporte(rs.getInt("id_reporte"));
                    Timestamp fecha = rs.getTimestamp("fecha_hora");
                    r.setFechaHora(fecha == null ? null : fecha.toLocalDateTime());
                    r.setEstado(rs.getString("estado_reporte"));
                    r.setNumeroTelefono(numeroConPrefijo(rs.getString("prefijo"), rs.getString("numero_telefono")));
                    lista.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reportes recientes: " + e.getMessage());
        }
        return lista;
    }

    public int contarMisReportes(int idUsuario, String estado) {
        String sql = "SELECT COUNT(*) FROM reporte r WHERE r.id_usuario = ?"
                + (estado.isEmpty() ? "" : " AND r.estado_reporte = ?");
        try (Connection con = ConexionDB.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            if (!estado.isEmpty()) ps.setString(2, estado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar mis reportes: " + e.getMessage());
        }
        return 0;
    }

    public List<ReporteComunidad> listarMisReportes(int idUsuario, String estado, int desde, int cantidad) {
        List<ReporteComunidad> lista = new ArrayList<>();
        String sql = "SELECT r.id_reporte, r.fecha_hora, r.cuerpo_sms, r.estado_reporte, nr.numero_telefono, nr.cantidad_reportes, pp.prefijo" +
                FROM_RECIENTES + " WHERE r.id_usuario = ?" +
                (estado.isEmpty() ? "" : " AND r.estado_reporte = ?") +
                " ORDER BY r.fecha_hora DESC, r.id_reporte DESC LIMIT ? OFFSET ?";
        try (Connection con = ConexionDB.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            int i = 1;
            ps.setInt(i++, idUsuario);
            if (!estado.isEmpty()) ps.setString(i++, estado);
            ps.setInt(i++, cantidad);
            ps.setInt(i, desde);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteComunidad r = new ReporteComunidad();
                    r.setIdReporte(rs.getInt("id_reporte"));
                    Timestamp fecha = rs.getTimestamp("fecha_hora");
                    r.setFechaHora(fecha == null ? null : fecha.toLocalDateTime());
                    r.setCuerpoSms(rs.getString("cuerpo_sms"));
                    r.setEstado(rs.getString("estado_reporte"));
                    r.setNumeroTelefono(numeroConPrefijo(rs.getString("prefijo"), rs.getString("numero_telefono")));
                    r.setCantidadReportesNumero(rs.getInt("cantidad_reportes"));
                    r.setIdUsuario(idUsuario);
                    lista.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar mis reportes: " + e.getMessage());
        }
        return lista;
    }

    // "+598" + "91204773" -> "+598 91204773". Si el número ya trae el "+", se deja como está.
    private String numeroConPrefijo(String prefijo, String numero) {
        if (numero == null) return "";
        if (prefijo == null || prefijo.isBlank() || numero.trim().startsWith("+")) return numero;
        String p = prefijo.trim();
        if (!p.startsWith("+")) p = "+" + p;
        return p + " " + numero.trim();
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
