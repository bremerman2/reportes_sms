package org.example;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Numero_oficialDAO {

    public static boolean existeNumeroOficial(String numeroTelefono) {
        String query = "SELECT COUNT(*) FROM numero_oficial WHERE numero_telefono = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setString(1, numeroTelefono);

            try (ResultSet rSet = sentencia.executeQuery()) {
                if (rSet.next()) {
                    return rSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar duplicados");
        }
        return false;
    }

    //verifica duplicados ignorando el numero que se esta editando
    public boolean existeNumeroOficialExcluyendoId(String numeroTelefono, int idExcluir) {
        String query = "SELECT COUNT(*) FROM numero_oficial WHERE numero_telefono = ? AND id_numero_oficial != ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setString(1, numeroTelefono);
            sentencia.setInt(2, idExcluir);

            try (ResultSet rSet = sentencia.executeQuery()) {
                if (rSet.next()) {
                    return rSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar duplicados");
        }
        return false;
    }

    public boolean registrarNumOficial(Numero_oficial numeroOficial) {

        //no se manda el id porque la columna es autoincremental
        String query = "INSERT INTO numero_oficial (numero_telefono, nombre_entidad, fecha_carga, id_prefijo, id_admin) VALUES(?, ?, ?,?,?)";

        //try-with-resources: la conexion se cierra sola al terminar (importante en la web, si no se agotan)
        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            sentencia.setString(1, numeroOficial.getNumero_telefono());
            sentencia.setString(2, numeroOficial.getNombre_entidad());
            sentencia.setDate(3, Date.valueOf(numeroOficial.getFecha_carga()));
            sentencia.setInt(4, numeroOficial.getPais_prefijo().getId_prefijo());
            sentencia.setInt(5, numeroOficial.getAdministrador().getId_administrador());

            boolean insertado = sentencia.executeUpdate() == 1;

            //recupera el id que genero la db y lo carga en el objeto
            if (insertado) {
                try (ResultSet generadas = sentencia.getGeneratedKeys()) {
                    if (generadas.next()) {
                        numeroOficial.setId_numero_oficial(generadas.getInt(1));
                    }
                }
            }

            return insertado;

        } catch (SQLException e) {
            System.out.println("Error al registrar numero oficial: " + e.getMessage());
            return false;
        }
    }

    public boolean editarNumOficial(Numero_oficial numeroOficial) {

        String query = "UPDATE numero_oficial SET numero_telefono = ?, nombre_entidad = ?, fecha_carga = ?, id_prefijo = ?, id_admin = ? WHERE id_numero_oficial = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setString(1, numeroOficial.getNumero_telefono());
            sentencia.setString(2, numeroOficial.getNombre_entidad());
            sentencia.setDate(3, Date.valueOf(numeroOficial.getFecha_carga()));
            sentencia.setInt(4, numeroOficial.getPais_prefijo().getId_prefijo());
            sentencia.setInt(5, numeroOficial.getAdministrador().getId_administrador());
            sentencia.setInt(6, numeroOficial.getId_numero_oficial());

            return sentencia.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al editar numero oficial: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarNumOficial(int idNumeroOficial) {

        String query = "DELETE FROM numero_oficial WHERE id_numero_oficial = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, idNumeroOficial);
            return sentencia.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al eliminar numero oficial: " + e.getMessage());
            return false;
        }
    }

    //busca un numero oficial por id (para cargar el formulario de edicion)
    public Numero_oficial obtenerPorId(int idNumeroOficial) {

        String query = """
    SELECT no.id_numero_oficial,
           no.numero_telefono,
           no.nombre_entidad,
           no.fecha_carga,
           pp.id_prefijo,
           pp.prefijo,
           pp.nombre_pais,
           no.id_admin
    FROM numero_oficial no
    INNER JOIN pais_prefijo pp
        ON no.id_prefijo = pp.id_prefijo
    WHERE no.id_numero_oficial = ?
    """;

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, idNumeroOficial);

            try (ResultSet rSet = sentencia.executeQuery()) {
                if (rSet.next()) {
                    Pais_prefijo prefijo = new Pais_prefijo(
                            rSet.getInt("id_prefijo"),
                            rSet.getString("prefijo"),
                            rSet.getString("nombre_pais")
                    );

                    Administrador administrador = new Administrador();
                    administrador.setId_administrador(rSet.getInt("id_admin"));

                    return new Numero_oficial(
                            rSet.getInt("id_numero_oficial"),
                            rSet.getString("numero_telefono"),
                            rSet.getString("nombre_entidad"),
                            rSet.getDate("fecha_carga").toLocalDate(),
                            prefijo,
                            administrador
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar numero oficial: " + e.getMessage());
        }
        return null;
    }

    public List<Numero_oficial> listarNumOficiales() {

        List<Numero_oficial> lista = new ArrayList<>();

        String query = """
    SELECT no.id_numero_oficial,
           no.numero_telefono,
           no.nombre_entidad,
           no.fecha_carga,
           pp.id_prefijo,
           pp.prefijo,
           pp.nombre_pais,
           a.id_admin AS id_administrador,
           a.nombre
    FROM numero_oficial no
    INNER JOIN pais_prefijo pp
        ON no.id_prefijo = pp.id_prefijo
    INNER JOIN administrador a
        ON no.id_admin = a.id_admin
    ORDER BY no.id_numero_oficial
    """;

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query);
             ResultSet rSet = sentencia.executeQuery()) {

            while (rSet.next()) {

                Pais_prefijo prefijo = new Pais_prefijo(
                        rSet.getInt("id_prefijo"),
                        rSet.getString("prefijo"),
                        rSet.getString("nombre_pais")
                );

                Administrador administrador = new Administrador();
                administrador.setId_administrador(
                        rSet.getInt("id_administrador")
                );
                administrador.setNombre(
                        rSet.getString("nombre")
                );

                Numero_oficial numero = new Numero_oficial(
                        rSet.getInt("id_numero_oficial"),
                        rSet.getString("numero_telefono"),
                        rSet.getString("nombre_entidad"),
                        rSet.getDate("fecha_carga").toLocalDate(),
                        prefijo,
                        administrador
                );

                lista.add(numero);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar numeros oficiales.");
            e.printStackTrace();
        }

        return lista;
    }
}
