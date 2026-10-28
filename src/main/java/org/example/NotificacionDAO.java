package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDAO {

    //REGISTRAR NOTIFICACION
    public boolean registrarNotificacion(Notificacion notificacion) {
        String query = "INSERT INTO notificaciones (id_usuario, mensaje, leido, enlace) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setInt(1, notificacion.getId_usuario());
            sentencia.setString(2, notificacion.getMensaje());
            sentencia.setBoolean(3, notificacion.isLeido());
            sentencia.setString(4, notificacion.getEnlace());

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet generadas = sentencia.getGeneratedKeys()) {
                    if (generadas.next()) {
                        notificacion.setId(generadas.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar notificacion: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public int contarNoLeidas(int idUsuario) {
        String query = "SELECT COUNT(*) FROM notificaciones WHERE id_usuario = ? AND leido = 0";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, idUsuario);

            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al contar notificaciones no leidas: " + e.getMessage());
        }
        return 0;
    }

    public List<Notificacion> obtenerPorUsuario(int idUsuario, int limite) {
        List<Notificacion> lista = new ArrayList<>();
        String query = "SELECT * FROM notificaciones WHERE id_usuario = ? ORDER BY fecha_creacion DESC LIMIT ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, idUsuario);
            sentencia.setInt(2, limite);

            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Notificacion n = new Notificacion();
                    n.setId(rs.getInt("id_notificacion"));
                    n.setId_usuario(rs.getInt("id_usuario"));
                    n.setMensaje(rs.getString("mensaje"));
                    n.setLeido(rs.getBoolean("leido"));
                    n.setEnlace(rs.getString("enlace"));
                    n.setFecha_creacion(rs.getTimestamp("fecha_creacion"));

                    lista.add(n);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar notificaciones: " + e.getMessage());
        }
        return lista;
    }

    public boolean marcarComoLeidas(int idUsuario) {
        String query = "UPDATE notificaciones SET leido = 1 WHERE id_usuario = ? AND leido = 0";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, idUsuario);
            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al marcar notificaciones como leidas: " + e.getMessage());
        }
        return false;
    }
}