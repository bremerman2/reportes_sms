package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReportesDAO {

    //REGISTRAR REPORTE (el estado siempre arranca en PENDIENTE)
    public boolean registrarReporte(Reportes reporte) {

        String query = "INSERT INTO reporte (fecha_hora, cuerpo_sms, captura_pantalla, es_manual, estado_reporte, id_usuario, id_num_reportado) VALUES(?,?,?,?,?,?,?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            sentencia.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            sentencia.setString(2, reporte.getCuerpo_sms());
            sentencia.setString(3, reporte.getCaptura_pantalla());
            sentencia.setBoolean(4, reporte.isEs_manual());
            sentencia.setString(5, estado_reporte.PENDIENTE.name().toLowerCase());
            sentencia.setInt(6, reporte.getUsuario().getId_usuario());
            sentencia.setInt(7, reporte.getNumero_reportado().getId_numero_reportado());

            boolean insertado = sentencia.executeUpdate() == 1;

            if (insertado) {
                ResultSet generadas = sentencia.getGeneratedKeys();
                if (generadas.next()) {
                    reporte.setId_reporte(generadas.getInt(1));
                }
                reporte.setEstado_reporte(estado_reporte.PENDIENTE);
            }
            return insertado;

        } catch (SQLException e) {
            System.out.println("Error al registrar reporte");
            return false;
        }
    }

    //CONFIRMAR REPORTE (pasa de PENDIENTE a CONFIRMADO)
    public boolean confirmarReporte(int idReporte) {

        String query = "UPDATE reporte SET estado_reporte = ? WHERE id_reporte = ? AND estado_reporte = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setString(1, estado_reporte.CONFIRMADO.name().toLowerCase());
            sentencia.setInt(2, idReporte);
            sentencia.setString(3, estado_reporte.PENDIENTE.name().toLowerCase());

            return sentencia.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error al confirmar reporte");
            return false;
        }
    }

    //GESTIONAR FALSO POSITIVO
    public boolean gestionarFalsoPositivo(int idReporte) {

        String query = "UPDATE reporte SET estado_reporte = ? WHERE id_reporte = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setString(1, estado_reporte.FALSO_POSITIVO.name().toLowerCase());
            sentencia.setInt(2, idReporte);

            return sentencia.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error al gestionar falso positivo del reporte");
            return false;
        }
    }

    //LISTAR TODOS LOS REPORTES
    public List<Reportes> listarReportes() {
        List<Reportes> lista = new ArrayList<>();

        String query = "SELECT * FROM reporte";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query);
             ResultSet rSet = sentencia.executeQuery()) {

            while (rSet.next()) {
                lista.add(mapearReporte(rSet));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar reportes");
        }
        return lista;
    }

    //BUSCAR REPORTES POR ESTADO
    public List<Reportes> buscarPorEstado(estado_reporte estado) {
        List<Reportes> lista = new ArrayList<>();

        String query = "SELECT * FROM reporte WHERE estado_reporte = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setString(1, estado.name().toLowerCase());

            try (ResultSet rSet = sentencia.executeQuery()) {
                while (rSet.next()) {
                    lista.add(mapearReporte(rSet));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar reportes por estado");
        }
        return lista;
    }

    //arma un Reporte a partir de una fila del ResultSet
    private Reportes mapearReporte(ResultSet rSet) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(rSet.getInt("id_usuario"));

        Numero_reportado numeroReportado = new Numero_reportado();
        numeroReportado.setId_numero_reportado(rSet.getInt("id_num_reportado"));

        Reportes reporte = new Reportes();
        reporte.setId_reporte(rSet.getInt("id_reporte"));
        reporte.setFecha_hora(rSet.getTimestamp("fecha_hora").toLocalDateTime());
        reporte.setCuerpo_sms(rSet.getString("cuerpo_sms"));
        reporte.setCaptura_pantalla(rSet.getString("captura_pantalla"));
        reporte.setEs_manual(rSet.getBoolean("es_manual"));
        reporte.setEstado_reporte(estado_reporte.valueOf(rSet.getString("estado_reporte").toUpperCase()));
        reporte.setUsuario(usuario);
        reporte.setNumero_reportado(numeroReportado);

        return reporte;
    }
}