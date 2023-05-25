package com.example.torneoloquesea;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Jugador {
    private int Ranking;
    private String Nombre;
    private int Fide;
    private int Id_Fide;
    private String Origen;
    private String Alojado;
    private boolean Participa;

    public Jugador(int Ranking, String Nombre, int Fide, int Id_Fide, String Origen, String Alojado, boolean Participa) {
        this.Ranking = Ranking;
        this.Nombre = Nombre;
        this.Fide = Fide;
        this.Id_Fide = Id_Fide;
        this.Origen = Origen;
        this.Alojado = Alojado;
        this.Participa = Participa;
    }

    public int getRanking() {
        return Ranking;
    }

    public String getNombre() {
        return Nombre;
    }

    public int getFide() {
        return Fide;
    }

    public int getId_Fide() {
        return Id_Fide;
    }

    public String getOrigen() {
        return Origen;
    }

    public String getAlojado() {
        return Alojado;
    }

    public boolean isParticipa() {
        return Participa;
    }

    public static ArrayList<Jugador> obtenerJugadores(Connection cnx){
        ArrayList<Jugador> jugadores = new ArrayList<>();
        try {
            ResultSet rsJ = cnx.createStatement().executeQuery("select * from jugador");
            rsJ.first();
            do {
                jugadores.add(new Jugador(rsJ.getInt(1), rsJ.getString(2), rsJ.getInt(3), rsJ.getInt(4), rsJ.getString(5), rsJ.getString(6), rsJ.getBoolean(7)));
            }while (rsJ.next());
        }catch (SQLException e){
            e.printStackTrace();
        }
        return jugadores;
    }

}
