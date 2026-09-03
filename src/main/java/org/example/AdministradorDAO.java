package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministradorDAO {
    //VERIFICAR EMAIL
    public boolean existeEmail(String email) {
        //generar query con consulta
        String query = "SELECT COUNT(*) FROM administrador WHERE LOWER(email) = LOWER(?)";
         
        try (Connection conexion = ConexionDB.obtenerConexion();
            PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setString(1, email.trim());
            
            //executeQuery ejecuta la consulta select, rSet almacena el resultado
            try (ResultSet rSet = sentencia.executeQuery()) {
                if(rSet.next()) {
                    return rSet.getInt(1) > 0;
                    //devuelve true si encontro coindidencia
                }  
            }
        } catch (SQLException e) {
            System.out.println("Error al validar existencia de email" + e.getMessage());        
        }
        return false;
    }
    //REGISTRAR ADMINISTRADOR
    public boolean registrarAdministrador(Administrador administrador) {

        String query = "INSERT INTO administrador (email, password, nombre, apellido, telefono) VALUES(?, ?, ?, ?, ?)";
        
        try (Connection conexion = ConexionDB.obtenerConexion();
            PreparedStatement sentencia = conexion.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);) {
            
            sentencia.setString(1, administrador.getEmail());
            sentencia.setString(2, administrador.getContrasenia());
            sentencia.setString(3, administrador.getNombre());
            sentencia.setString(4, administrador.getApellido());
            sentencia.setString(5, administrador.getTelefono());

            //ejecuta query
            boolean insertado = sentencia.executeUpdate() == 1;

            //recupera el id generado por la bd, cargandolo en el objeto
            if (insertado) {
                ResultSet generadas = sentencia.getGeneratedKeys();
                if (generadas.next()) {
                    administrador.setId_administrador(generadas.getInt(1));
                }
            }
            return insertado;

        } catch (SQLException e) {
            System.out.println("Error al registrar administrador" + e.getMessage());
            return false;
        }
    }
}
