package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UsuarioDAO {

    public Usuario obtenerUsuarioPorId(int idUsuario) {

        String query = """
                SELECT id_usuario,
                       bloqueado,
                       nombre,
                       apellido,
                       telefono,
                       email,
                       password,
                       id_admin
                FROM usuario
                WHERE id_usuario = ?
                """;

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, idUsuario);

            try (ResultSet rSet = sentencia.executeQuery()) {

                if (rSet.next()) {

                    Administrador administrador = new Administrador();
                    administrador.setId_administrador(
                            rSet.getInt("id_admin")
                    );

                    Usuario usuario = new Usuario();

                    usuario.setId_usuario(
                            rSet.getInt("id_usuario")
                    );

                    usuario.setBloqueado(
                            rSet.getBoolean("bloqueado")
                    );

                    usuario.setNombre(
                            rSet.getString("nombre")
                    );

                    usuario.setApellido(
                            rSet.getString("apellido")
                    );

                    usuario.setTelefono(
                            rSet.getString("telefono")
                    );

                    usuario.setEmail(
                            rSet.getString("email")
                    );

                    usuario.setPassword(
                            rSet.getString("password")
                    );

                    usuario.setAdministrador(administrador);

                    return usuario;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario.");
            e.printStackTrace();
        }

        return null;
    }

    public boolean bloquearUsuario(int idUsuario) {

        String query = """
                UPDATE usuario
                SET bloqueado = TRUE
                WHERE id_usuario = ?
                """;

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, idUsuario);

            return sentencia.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al bloquear usuario.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarUsuario(int idUsuario) {

        String query = """
                DELETE FROM usuario
                WHERE id_usuario = ?
                """;

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, idUsuario);

            return sentencia.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario.");
            e.printStackTrace();
            return false;
        }
    }

    public int[] obtenerEstadisticasUsuarios() {
        String query = "SELECT " + "COUNT(*) AS total, " + 
                   "SUM(CASE WHEN bloqueado = 1 THEN 1 ELSE 0 END) AS bloqueados, " + 
                   "SUM(CASE WHEN bloqueado = 0 THEN 1 ELSE 0 END) AS activos " + "FROM usuario";

        try (Connection conexion = ConexionDB.obtenerConexion(); 
            PreparedStatement sentencia = conexion.prepareStatement(query);
            ResultSet rSet = sentencia.executeQuery()) {

        if (rSet.next()) {
            return new int[] {
                rSet.getInt("total"),
                rSet.getInt("bloqueados"),
                rSet.getInt("activos")
            };
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener estadísticas: " + e.getMessage());
    }
    return new int[] {0, 0, 0};
}

}
