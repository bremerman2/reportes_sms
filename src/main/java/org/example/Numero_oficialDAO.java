package org.example;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Numero_oficialDAO {

    public boolean registrarNumOficial(Numero_oficial numeroOficial) {
        try {
            //1-se conecta a la db
            Connection conexion = ConexionDB.obtenerConexion();

            //2-arma la query
            String query = "INSERT INTO numero_oficial (id_numero_oficial, numero_telefono, fecha_carga, id_prefijo, id_admin) VALUES(?,?,?,?,?)";
            PreparedStatement sentencia = conexion.prepareStatement(query);
            sentencia.setInt(1, numeroOficial.getId_numero_oficial());
            sentencia.setString(2, numeroOficial.getNumero_telefono());
            sentencia.setDate(3, Date.valueOf(numeroOficial.getFecha_carga()));
            sentencia.setInt(4, numeroOficial.getPais_prefijo().getId_prefijo());
            sentencia.setInt(5, numeroOficial.getAdministrador().getId_administrador());

            //3-ejecuta query
            return sentencia.executeUpdate() == 1;

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
            String query = "UPDATE numero_oficial SET numero_telefono = ?, fecha_carga = ?, id_prefijo = ?, id_admin = ? WHERE id_numero_oficial = ?";
            PreparedStatement sentencia = conexion.prepareStatement(query);
            sentencia.setString(1, numeroOficial.getNumero_telefono());
            sentencia.setDate(2, Date.valueOf(numeroOficial.getFecha_carga()));
            sentencia.setInt(3, numeroOficial.getPais_prefijo().getId_prefijo());
            sentencia.setInt(4, numeroOficial.getAdministrador().getId_administrador());
            sentencia.setInt(5, numeroOficial.getId_numero_oficial());

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