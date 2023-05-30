package com.example.torneoloquesea;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    private String Participa;

    public Jugador(int Ranking, String Nombre, int Fide, int Id_Fide, String Origen, String Alojado, String Participa) {
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

    public String getParticipa() {
        return Participa;
    }

    public static ArrayList<Jugador> obtenerJugadores(Connection cnx){
        ArrayList<Jugador> jugadores = new ArrayList<>();
        try {
            ResultSet rsJ = cnx.createStatement().executeQuery("select * from jugador");
            rsJ.first();
            String participa;
            do {
                if (rsJ.getBoolean(7)){
                    participa = "Si";
                }else{
                    participa = "No";
                }
                jugadores.add(new Jugador(rsJ.getInt(1), rsJ.getString(2), rsJ.getInt(3), rsJ.getInt(4), rsJ.getString(5), rsJ.getString(6), participa));
            }while (rsJ.next());
            rsJ.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return jugadores;
    }

    public static ResultSet buscarJugadorUnico(int Ranking, Connection cnx){
        try {
            return cnx.createStatement().executeQuery("select Nombre, Origen, Alojado, Participa from jugador where Ranking = " + Ranking);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean modificarJugador(int Ranking, String Nombre, String Origen, String Alojado, String Participa, Connection cnx){
        try{
            PreparedStatement ps = cnx.prepareStatement("update jugador set Nombre = ?, Origen = ?, Alojado = ?, Participa = ? where Ranking = ?");
            boolean boolParticipa;
            boolParticipa = Participa.equals("Si");
            ps.setString(1, Nombre);
            ps.setString(2, Origen);
            ps.setString(3, Alojado);
            ps.setBoolean(4, boolParticipa);
            ps.setInt(5, Ranking);
            int cambio = ps.executeUpdate();
            Torneo.generarDatosOpta(cnx);
            return cambio == 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }
}
