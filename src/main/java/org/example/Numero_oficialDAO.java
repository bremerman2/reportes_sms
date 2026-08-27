package org.example;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Numero_oficialDAO {

    public boolean registrarNumOficial(Numero_oficial numeroOficial) {
        try {
            //1-se conecta a la db
            Connection conexion = ConexionDB.obtenerConexion();

            //2-arma la query (no se manda el id porque la columna es autoincremental)
            String query = "INSERT INTO numero_oficial (numero_telefono, nombre_entidad, fecha_carga, id_prefijo, id_admin) VALUES(?, ?, ?,?,?)";
            PreparedStatement sentencia = conexion.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, numeroOficial.getNumero_telefono());
            sentencia.setString(2, numeroOficial.getNombre_entidad());
            sentencia.setDate(3, Date.valueOf(numeroOficial.getFecha_carga()));
            sentencia.setInt(4, numeroOficial.getPais_prefijo().getId_prefijo());
            sentencia.setInt(5, numeroOficial.getAdministrador().getId_administrador());

            //3-ejecuta query
            boolean insertado = sentencia.executeUpdate() == 1;

            //4-recupera el id que genero la db y lo carga en el objeto
            if (insertado) {
                ResultSet generadas = sentencia.getGeneratedKeys();
                if (generadas.next()) {
                    numeroOficial.setId_numero_oficial(generadas.getInt(1));
                }
            }

            return insertado;

        } catch (SQLException e) {
            System.out.println("Error al registrar numero oficial");
            throw new RuntimeException(e);
        }
    }

    public boolean editarNumOficial(Numero_oficial numeroOficial) {
        try {
            //1-se conecta a la db
            Connection conexion = ConexionDB.obtenerConexion();

            //2-arma la query
            String query = "UPDATE numero_oficial SET numero_telefono = ?, nombre_entidad = ?, fecha_carga = ?, id_prefijo = ?, id_admin = ? WHERE id_numero_oficial = ?";
            PreparedStatement sentencia = conexion.prepareStatement(query);
            sentencia.setString(1, numeroOficial.getNumero_telefono());
            sentencia.setString(2, numeroOficial.getNombre_entidad());
            sentencia.setDate(3, Date.valueOf(numeroOficial.getFecha_carga()));
            sentencia.setInt(4, numeroOficial.getPais_prefijo().getId_prefijo());
            sentencia.setInt(5, numeroOficial.getAdministrador().getId_administrador());
            sentencia.setInt(6, numeroOficial.getId_numero_oficial());

            //3-ejecuta query
            return sentencia.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al editar numero oficial");
            throw new RuntimeException(e);
        }
    }

    public boolean eliminarNumOficial(int idNumeroOficial) {
        try {
            //1-se conecta a la db
            Connection conexion = ConexionDB.obtenerConexion();

            //2-arma la query
            String query = "DELETE FROM numero_oficial WHERE id_numero_oficial = ?";
            PreparedStatement sentencia = conexion.prepareStatement(query);
            sentencia.setInt(1, idNumeroOficial);

            //3-ejecuta query
            return sentencia.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al eliminar numero oficial");
            throw new RuntimeException(e);
        }
    }
}