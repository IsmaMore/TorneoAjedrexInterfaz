package com.example.torneoloquesea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Clase Jugador. Se utiiza para crear objetos de los datos devueltos de la tabla "jugador" en la base de datos.
 *
 * @version     1.0 24/05/2023
 * @author      Ismael Moreno
 */
public class Jugador {
    private int Ranking;
    private String Nombre;
    private int Fide;
    private int Id_Fide;
    private String Origen;
    private String Alojado;
    private String Participa;

    /**
     * Constructor de Jugador.
     *
     * @param Ranking int
     * @param Nombre String
     * @param Fide int
     * @param Id_Fide int
     * @param Origen String
     * @param Alojado String
     * @param Participa String
     */
    public Jugador(int Ranking, String Nombre, int Fide, int Id_Fide, String Origen, String Alojado, String Participa) {
        this.Ranking = Ranking;
        this.Nombre = Nombre;
        this.Fide = Fide;
        this.Id_Fide = Id_Fide;
        this.Origen = Origen;
        this.Alojado = Alojado;
        this.Participa = Participa;
    }

    /**
     * @return int
     */
    public int getRanking() {
        return Ranking;
    }

    /**
     * @return String
     */
    public String getNombre() {
        return Nombre;
    }

    /**
     * @return int
     */
    public int getFide() {
        return Fide;
    }

    /**
     * @return int
     */
    public int getId_Fide() {
        return Id_Fide;
    }

    /**
     * @return String
     */
    public String getOrigen() {
        return Origen;
    }

    /**
     * @return String
     */
    public String getAlojado() {
        return Alojado;
    }

    /**
     * @return String
     */
    public String getParticipa() {
        return Participa;
    }

    /**
     * Metodo que devuelve un arrayList de los jugadores que se encuentran en la tabla jugador diferenciando la conexion que indica que base de datos (Torneo).
     *
     * @param cnx Connection
     * @return ArrayList<Jugador>
     */
    public static ArrayList<Jugador> obtenerJugadores(Connection cnx){
        ArrayList<Jugador> jugadores = new ArrayList<>();
        try {
            ResultSet rsJ = cnx.createStatement().executeQuery("select * from jugador");
            if (rsJ.first()){
                String participa;
                do {
                    if (rsJ.getBoolean(7)) {
                        participa = "Si";
                    } else {
                        participa = "No";
                    }
                    jugadores.add(new Jugador(rsJ.getInt(1), rsJ.getString(2), rsJ.getInt(3), rsJ.getInt(4), rsJ.getString(5), rsJ.getString(6), participa));
                } while (rsJ.next());
            }
            rsJ.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return jugadores;
    }

    /**
     * Metedo que devuelve un ResultSet de un solo jugador dependiendo del Ranking.
     *
     * @param Ranking int
     * @param cnx Connection
     * @return ResultSet
     */
    public static ResultSet buscarJugadorUnico(int Ranking, Connection cnx){
        try {
            return cnx.createStatement().executeQuery("select Nombre, Origen, Alojado, Participa, Ranking from jugador where Ranking = " + Ranking);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metedo que devuelve un ResultSet uno o varios jugadores dependiendo del nombre.
     *
     * @param Nombre String
     * @param cnx Connection
     * @return ResultSet
     */
    public static ResultSet buscarJugadorPorNombre(String Nombre, Connection cnx){
        Nombre = "\"" + "%" + Nombre + "%" + "\"";
        try {
            return cnx.createStatement().executeQuery("select Nombre, Origen, Alojado, Participa, Ranking from jugador where Nombre like " + Nombre);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metodo que modifica los datos de un solo jugador devuelve un booleano dependiendo de si lo consigue o no.
     *
     * @param Ranking int
     * @param Nombre String
     * @param Origen String
     * @param Alojado String
     * @param Participa String
     * @param cnx Connection
     * @return boolean
     */
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
