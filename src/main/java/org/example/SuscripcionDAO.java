package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SuscripcionDAO {
    //REGISTRAR SUSCRIPCION
    public boolean registrarSuscripcion(Suscripcion suscripcion) {
        String query = "INSERT INTO suscripciones (id_usuario, id_reporte, fecha_suscripcion) VALUES (?, ?, NOW())";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, suscripcion.getId_usuario());
            sentencia.setInt(2, suscripcion.getId_reporte());

            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error SQL al registrar suscripción: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminarSuscripcion(int idUsuario, int idReporte) {
        String query = "DELETE FROM suscripciones WHERE id_usuario = ? AND id_reporte = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, idUsuario);
            sentencia.setInt(2, idReporte);

            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar suscripción: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    public boolean estaSuscripto(int idUsuario, int idReporte) {
        String query = "SELECT COUNT(*) FROM suscripciones WHERE id_usuario = ? AND id_reporte = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, idUsuario);
            sentencia.setInt(2, idReporte);

            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar suscripción: " + e.getMessage());
        }
        return false;
    }

    public List<Integer> obtenerUsuariosSuscriptos(int idReporte) {
        List<Integer> listaUsuarios = new ArrayList<>();
        String query = "SELECT id_usuario FROM suscripciones WHERE id_reporte = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, idReporte);

            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    listaUsuarios.add(rs.getInt("id_usuario"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener suscriptores: " + e.getMessage());
        }
        return listaUsuarios;
    }
}
