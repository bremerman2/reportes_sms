package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Pais_prefijoDAO {
    //VERIFICAR PREFIJO y PAIS
    public boolean existePrefijoYPais(String prefijo, String nombrePais) {
        //generar query con consulta
        String query = "SELECT COUNT(*) FROM pais_prefijo WHERE prefijo = ? AND LOWER(nombre_pais) = LOWER(?)";
         
        try (Connection conexion = ConexionDB.obtenerConexion();
            PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setString(1, prefijo);
            sentencia.setString(2, nombrePais);
            
            //executeQuery ejecuta la consulta select, rSet almacena el resultado
            try (ResultSet rSet = sentencia.executeQuery()) {
                if(rSet.next()) {
                    return rSet.getInt(1) > 0;
                    //devuelve true si encontro coindidencia
                }  
            }
        } catch (SQLException e) {
            System.out.println("Error al validar duplicados");        
        }
        return false;
    }

    //REGISTRAR PREFIJO
    public boolean registrarPaisPrefijo(Pais_prefijo paisPrefijo) {

        String query = "INSERT INTO pais_prefijo (prefijo, nombre_pais) VALUES(?, ?)";
        
        try (Connection conexion = ConexionDB.obtenerConexion();
            PreparedStatement sentencia = conexion.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);) {
            
            sentencia.setString(1, paisPrefijo.getPrefijo());
            sentencia.setString(2, paisPrefijo.getNombre_pais());

            boolean insertado = sentencia.executeUpdate() == 1;

            if (insertado) {
                ResultSet generadas = sentencia.getGeneratedKeys();
                if (generadas.next()) {
                    paisPrefijo.setId_prefijo(generadas.getInt(1));
                }
            }
            return insertado;

        } catch (SQLException e) {
            System.out.println("Error al registrar prefijo");
            return false;
        }
    }

    //EDITAR PREFIJO
    public boolean editarPrefijo(Pais_prefijo paisPrefijo) {

        String query = "UPDATE pais_prefijo SET prefijo = ?, nombre_pais = ? WHERE id_prefijo = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
            PreparedStatement sentencia = conexion.prepareStatement(query);) {

            sentencia.setString(1, paisPrefijo.getPrefijo());
            sentencia.setString(2, paisPrefijo.getNombre_pais());
            sentencia.setInt(3, paisPrefijo.getId_prefijo());

            return sentencia.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error al editar prefijo");
            return false;
        }
    }
    //ELIMINAR PREFIJO
    public boolean eliminarPrefijo(int id_prefijo) {

        String query = "DELETE FROM pais_prefijo WHERE id_prefijo = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
            PreparedStatement sentencia = conexion.prepareStatement(query);) {

                sentencia.setInt(1,id_prefijo);
                return sentencia.executeUpdate() == 1;
            } catch (SQLException e) {
                System.out.println("Error al eliminar prefijo");
                return false;
            }
    }

    //metodo para listar prefijos
    public List<Pais_prefijo> obtenerPrefijos() {
        List<Pais_prefijo> lista = new ArrayList<>();
    
        String query = "SELECT id_prefijo, prefijo, nombre_pais FROM pais_prefijo";

        try (Connection conexion = ConexionDB.obtenerConexion();
            PreparedStatement sentencia = conexion.prepareStatement(query);
            //se ejecuta executeQuery para consulta select
            ResultSet rSet = sentencia.executeQuery();) {

            while (rSet.next()) {
                int id = rSet.getInt("id_prefijo");
                String prefijo = rSet.getString("prefijo");
                String nombrePais = rSet.getString("nombre_pais");

                Pais_prefijo p = new Pais_prefijo(id, prefijo, nombrePais);
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar prefijos: ");
        }
        return lista;
    }
    //obtener prefijo buscado por id
    public Pais_prefijo obtenerPrefijoPorId(int id) {
    
    String query = "SELECT id_prefijo, prefijo, nombre_pais FROM pais_prefijo WHERE id_prefijo = ?";
    
    try (Connection conexion = ConexionDB.obtenerConexion();
         PreparedStatement sentencia = conexion.prepareStatement(query)) {

        sentencia.setInt(1, id);

        try (ResultSet rSet = sentencia.executeQuery()) {
            if(rSet.next()) {
                //si se encontro, se retorna el prefijo
                return new Pais_prefijo(rSet.getInt("id_prefijo"),rSet.getString("prefijo"), rSet.getString("nombre_pais"));
            }
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener prefijo");
    }
        return null; 
    }
}

