package com.example.torneoloquesea;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase Premio. Se utiiza para crear objetos de los datos devueltos de la tabla "premio" en la base de datos.
 *
 * @version     1.0 27/05/2023
 * @author      Ismael Moreno
 */
public class Premio {
    private int Posicion;
    private String Nombre;
    private String Tipo_Premio;
    private String Cantidad;

    /**
     * Constructor de Premio.
     *
     * @param posicion int
     * @param nombre String
     * @param tipo_Premio String
     * @param cantidad String
     */
    public Premio(int posicion, String nombre, String tipo_Premio, String cantidad) {
        Posicion = posicion;
        Nombre = nombre;
        Tipo_Premio = tipo_Premio;
        Cantidad = cantidad;
    }

    public int getPosicion() {
        return Posicion;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getTipo_Premio() {
        return Tipo_Premio;
    }

    public String getCantidad() {
        return Cantidad;
    }

    /**
     * Hace una seleccion de todos los premios(quien lo gana, cantidad, tipo y posicion) haciendo uso de una sentencia que une tres tablas diferentes.
     *
     * @param cnx Connection
     * @return ArrayList<Premio>
     */
    public static ArrayList<Premio> obtenerPremios(Connection cnx){
        ArrayList<Premio> premios = new ArrayList<>();
        try {
            ResultSet rsP = cnx.createStatement().executeQuery("select Posicion, Nombre, Tipo_Premio, Cantidad from clasificacion inner join jugador on clasificacion.Ranking=jugador.Ranking inner join premio on clasificacion.Id_Premio=premio.Id_Premio inner join tipoPremio on premio.Id_Tipo_Premio=tipoPremio.Id_Tipo_Premio order by Tipo_Premio");
            rsP.first();
            do {
                premios.add(new Premio(rsP.getInt(1), rsP.getString(2), rsP.getString(3), (rsP.getInt(4) + "€") ));
            }while (rsP.next());
            rsP.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return premios;
    }

    /**
     * Metodo que devuelve una lista de todos los tipos de premio a los que opta un jugador especifico.
     *
     * @param cnx Connection
     * @param Ranking int
     * @return ArrayList<String>
     */
    public static ArrayList<String> obtenerTipoPremioOpta(Connection cnx, int Ranking){
        ArrayList<String> tipoOpta = new ArrayList<>();
        try {
            ResultSet rsO = cnx.createStatement().executeQuery("select Tipo_Premio from tipoPremio inner join opta on tipoPremio.Id_Tipo_Premio=opta.Id_Tipo_Premio where Id_Ranking = " + Ranking);
            if (rsO.first()){
                do {
                    tipoOpta.add(rsO.getString(1));
                }while (rsO.next());
            }else {
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return tipoOpta;
    }
}
