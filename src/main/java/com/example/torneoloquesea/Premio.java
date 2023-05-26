package com.example.torneoloquesea;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Premio {
    private int Posicion;
    private String Nombre;
    private String Tipo_Premio;
    private String Cantidad;

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

    public static ArrayList<Premio> obtenerPremios(Connection cnx){
        ArrayList<Premio> premios = new ArrayList<>();
        try {
            ResultSet rsP = cnx.createStatement().executeQuery("select Posicion, Nombre, Tipo_Premio, Cantidad from clasificacion left join jugador on clasificacion.Ranking=jugador.Ranking left join premio on clasificacion.Id_Premio=premio.Id_Premio left join tipoPremio on premio.Id_Tipo_Premio=tipoPremio.Id_Tipo_Premio order by Posicion");
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
}
