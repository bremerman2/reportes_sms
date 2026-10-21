package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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


    // LOGIN DE USUARIO: no permite el acceso a cuentas bloqueadas.
    public Usuario buscarPorCredenciales(String email, String password) {
        String query = """
                SELECT id_usuario, bloqueado, nombre, apellido, telefono, email, password, id_admin
                FROM usuario
                WHERE LOWER(email) = LOWER(?) AND password = ? AND bloqueado = FALSE
                """;

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {
            sentencia.setString(1, email.trim());
            sentencia.setString(2, password);

            try (ResultSet rSet = sentencia.executeQuery()) {
                if (rSet.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId_usuario(rSet.getInt("id_usuario"));
                    usuario.setBloqueado(rSet.getBoolean("bloqueado"));
                    usuario.setNombre(rSet.getString("nombre"));
                    usuario.setApellido(rSet.getString("apellido"));
                    usuario.setTelefono(rSet.getString("telefono"));
                    usuario.setEmail(rSet.getString("email"));
                    usuario.setPassword(rSet.getString("password"));
                    Administrador admin = new Administrador();
                    int idAdmin = rSet.getInt("id_admin");
                    if (!rSet.wasNull()) {
                        admin.setId_administrador(idAdmin);
                        usuario.setAdministrador(admin);
                    }
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al iniciar sesión de usuario: " + e.getMessage());
        }
        return null;
    }

    public boolean existeEmail(String email, Integer idExcluir) {
        String query = "SELECT COUNT(*) FROM usuario WHERE LOWER(email) = LOWER(?)"
                + (idExcluir == null ? "" : " AND id_usuario <> ?");
        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {
            sentencia.setString(1, email.trim());
            if (idExcluir != null) sentencia.setInt(2, idExcluir);
            try (ResultSet rs = sentencia.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al comprobar email: " + e.getMessage());
            return true; // Ante un error, no permitir una operación potencialmente duplicada.
        }
    }

    public boolean registrarUsuario(Usuario usuario) {
        String query = """
                INSERT INTO usuario (bloqueado, nombre, apellido, telefono, email, password)
                VALUES (FALSE, ?, ?, ?, ?, ?)
                """;
        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getApellido());
            sentencia.setString(3, usuario.getTelefono());
            sentencia.setString(4, usuario.getEmail());
            sentencia.setString(5, usuario.getPassword());
            return sentencia.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarPerfil(Usuario usuario, boolean cambiarPassword) {
        String query = cambiarPassword
                ? "UPDATE usuario SET nombre=?, apellido=?, telefono=?, email=?, password=? WHERE id_usuario=? AND bloqueado=FALSE"
                : "UPDATE usuario SET nombre=?, apellido=?, telefono=?, email=? WHERE id_usuario=? AND bloqueado=FALSE";
        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getApellido());
            sentencia.setString(3, usuario.getTelefono());
            sentencia.setString(4, usuario.getEmail());
            if (cambiarPassword) {
                sentencia.setString(5, usuario.getPassword());
                sentencia.setInt(6, usuario.getId_usuario());
            } else {
                sentencia.setInt(5, usuario.getId_usuario());
            }
            return sentencia.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error al actualizar perfil: " + e.getMessage());
            return false;
        }
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

    //LISTAR USUARIOS (para la tabla de la pagina de usuarios)
    public List<Usuario> listarUsuarios() {

        List<Usuario> lista = new ArrayList<>();

        String query = "SELECT id_usuario, bloqueado, nombre, apellido, telefono, email FROM usuario ORDER BY id_usuario";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query);
             ResultSet rSet = sentencia.executeQuery()) {

            while (rSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rSet.getInt("id_usuario"));
                usuario.setBloqueado(rSet.getBoolean("bloqueado"));
                usuario.setNombre(rSet.getString("nombre"));
                usuario.setApellido(rSet.getString("apellido"));
                usuario.setTelefono(rSet.getString("telefono"));
                usuario.setEmail(rSet.getString("email"));
                lista.add(usuario);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
}
