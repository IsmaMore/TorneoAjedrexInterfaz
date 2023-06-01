package com.example.torneoloquesea;

import java.io.*;
import java.sql.*;
import java.util.*;



public class Torneo {

    static Connection cnxA;
    static Connection cnxB;
    final static String CSV_JUG_A = "datos/jugadorA.csv";
    final static String CSV_JUG_B = "datos/jugadorB.csv";
    final static String CSV_PRE_A = "datos/premioA.csv";
    final static String CSV_PRE_B = "datos/premioB.csv";
    final static String CSV_CLA_A = "datos/clasificacionA.csv";
    final static String CSV_CLA_B = "datos/clasificacionB.csv";

    static {
        try {
            cnxA = DriverManager.getConnection("jdbc:mariadb://localhost:3306/torneoA", "root", "root");
            cnxB = DriverManager.getConnection("jdbc:mariadb://localhost:3306/torneoB", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int buscarIdTipoPremio(String aux, Connection cnx){
        try {
            ResultSet rs = cnx.createStatement().executeQuery("select * from tipoPremio");
            rs.first();

            do {
                if (aux.equals(rs.getString(2))){
                    return rs.getInt(1);
                }
            }while (rs.next());
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public static void generarDatosPremio(Connection cnx, String csvFile){
        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFile));
            Statement st = cnx.createStatement();
            st.executeUpdate("delete from premio");
            String line;
            int cont = 0;
            while ((line = lineReader.readLine()) != null){
                String[] data = line.split("\\|");
                String Id_Premio = data[0];
                String aux = data[1];
                String Cantidad = data[2];
                int id_Tipo_Premio = buscarIdTipoPremio(aux.substring(1, aux.length()-1), cnx);

                int res = st.executeUpdate("insert into premio values(" + Id_Premio + ", " + id_Tipo_Premio + ", " + Cantidad + ")");
                if (res != 0) {
                    cont++;
                }
            }
            System.out.println("Se ha añadido " + cont + " filas");
        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
    }

    public static void generarDatosClasificacion(Connection cnx, String csvFile){
        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFile));
            Statement st = cnx.createStatement();
            st.executeUpdate("delete from clasificacion");
            String line;
            int cont = 0;
            while ((line = lineReader.readLine()) != null){
                //System.out.println(line);
                String[] data = line.split("\\|");
                String Posicion = data[0];
                String Ranking = data[1];
                int res = st.executeUpdate("insert into clasificacion values(" + Posicion + ", " + Ranking + ", default )");
                if (res != 0) {
                    cont++;
                }
            }
            System.out.println("Se ha añadido " + cont + " filas");
        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
    }

    public static void generarDatosJugador(Connection cnx, String csvFile){
        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFile));
            Statement st = cnx.createStatement();
            st.executeUpdate("delete from jugador");
            String line;
            int cont = 0;
            while ((line = lineReader.readLine()) != null){
                //System.out.println(line);
                String[] data = line.split("\\|");
                String Ranking = data[0];
                String Nombre = data[1];
                String FIDE = data[2];
                String Id_FIDE = data[3];
                String Origen = data[4];
                String Alojado = data[5];
                if (Origen.isEmpty()){
                    Origen = null;
                }
                int res = st.executeUpdate("insert into jugador values(" + Ranking + ", " + Nombre + ", " + FIDE + ", " + Id_FIDE + ", " + Origen + ", " + Alojado + ", default )");
                if (res != 0) {
                    cont++;
                }
            }
            System.out.println("Se ha añadido " + cont + " filas");
        }catch (SQLException | IOException e){
            e.printStackTrace();
        }
    }

    public static void generarDatosOpta(Connection cnx){
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate("delete from opta");
            ResultSet rsJ = st.executeQuery("select * from jugador");
            ResultSet rsTP = st.executeQuery("select * from tipoPremio");
            PreparedStatement ps = cnx.prepareStatement("insert into opta values (?, ?)");
            rsJ.first();
            rsTP.first();
            do {
                ps.setInt(1, rsJ.getInt(1));
                ps.setInt(2, rsTP.getInt(1));
                ps.executeUpdate();
                rsTP.next();
                if (rsJ.getString(5) != null && rsJ.getString(5).equals(rsTP.getString(2))){
                    ps.setInt(1, rsJ.getInt(1));
                    ps.setInt(2, rsTP.getInt(1));
                    ps.executeUpdate();
                }
                while (rsTP.next() && rsTP.getString(2).contains("SUB")){
                    String str = rsTP.getString(2).substring(4);
                    if (rsJ.getInt(3) < Integer.parseInt(str)){
                        ps.setInt(1, rsJ.getInt(1));
                        ps.setInt(2, rsTP.getInt(1));
                        ps.executeUpdate();
                    }
                }
                if (rsJ.getString(6).equals("Si")){
                    ps.setInt(1, rsJ.getInt(1));
                    ps.setInt(2, rsTP.getInt(1));
                    ps.executeUpdate();
                }
                rsTP.first();
            }while (rsJ.next());
            rsJ.close();
            rsTP.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void generarPremioEnClasificacion(Connection cnx){
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate("update clasificacion set Id_Premio = null");
            PreparedStatement psActualizar = cnx.prepareStatement("update clasificacion set Id_Premio = ? where Ranking = ?");
            PreparedStatement psO = cnx.prepareStatement("select * from opta where Id_Ranking = ?");
            PreparedStatement psP = cnx.prepareStatement("select * from premio where Id_Tipo_Premio = ?");
            PreparedStatement psPAsignado = cnx.prepareStatement("select * from clasificacion where Id_Premio = ?");
            PreparedStatement psParticipa = cnx.prepareStatement("select Participa from jugador where Ranking = ?");
            ResultSet rsC = st.executeQuery("select * from clasificacion");
            ResultSet rsP = st.executeQuery("select * from premio");
            rsC.first();
            rsP.first();

            do {
                psParticipa.setInt(1, rsC.getInt(2));
                ResultSet rsParticipa = psParticipa.executeQuery();
                rsParticipa.first();
                if (rsParticipa.getBoolean(1)) {
                    rsParticipa.close();
                    ArrayList<Integer> ids = new ArrayList<>();
                    ArrayList<Integer> cant = new ArrayList<>();
                    psO.setInt(1, rsC.getInt(2));
                    ResultSet rsOJ = psO.executeQuery();
                    rsOJ.first();
                    do {
                        psP.setInt(1, rsOJ.getInt(2));
                        ResultSet rsPOJ = psP.executeQuery();
                        rsPOJ.first();
                        do {
                            psPAsignado.setInt(1, rsPOJ.getInt(1));
                            ResultSet rsAux = psPAsignado.executeQuery();
                            if (!rsAux.first()) {
                                ids.add(rsPOJ.getInt(1));
                                cant.add(rsPOJ.getInt(3));
                                rsPOJ.last();
                            }
                        } while (rsPOJ.next());
                        rsPOJ.close();
                    } while (rsOJ.next());
                    rsOJ.close();
                    int sel = 0;
                    int cantSel = 0;
                    if (ids.size() == 1) {
                        psActualizar.setInt(2, rsC.getInt(2));
                        psActualizar.setInt(1, ids.get(0));
                        psActualizar.executeUpdate();
                    } else if (ids.size() > 1) {
                        for (int i = 0; i < ids.size(); i++) {
                            if (cantSel < cant.get(i)) {
                                sel = ids.get(i);
                                cantSel = cant.get(i);
                            }
                        }
                        psActualizar.setInt(2, rsC.getInt(2));
                        psActualizar.setInt(1, sel);
                        psActualizar.executeUpdate();
                    }
                }
            }while (rsC.next());
            rsC.close();
            rsP.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void ejecutarMain(){
        main(null);
    }

    public static void ejecutarGenerarClasificacionA(){
        generarPremioEnClasificacion(cnxA);
    }

    public static void ejecutarGenerarClasificacionB(){
        generarPremioEnClasificacion(cnxB);
    }

    public static void ejecutarGenerarOptaA(){
        generarDatosOpta(cnxA);
    }

    public static void ejecutarGenerarOptaB(){
        generarDatosOpta(cnxB);
    }

    public static void ejecutarGenerarJugadoresA(){
        try {
            cnxA.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");
            generarDatosJugador(cnxA, CSV_JUG_A);
            cnxA.createStatement().execute("SET FOREIGN_KEY_CHECKS=1");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void ejecutarGenerarJugadoresB(){
        try {
            cnxB.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");
            generarDatosJugador(cnxB, CSV_JUG_B);
            cnxB.createStatement().execute("SET FOREIGN_KEY_CHECKS=1");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void ejecutarImportarClasificacionA(){
        try {
            cnxB.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");
            generarDatosClasificacion(cnxA, CSV_CLA_A);
            cnxB.createStatement().execute("SET FOREIGN_KEY_CHECKS=1");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void ejecutarImportarClasificacionB(){
        try {
            cnxB.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");
            generarDatosClasificacion(cnxB, CSV_CLA_B);
            cnxB.createStatement().execute("SET FOREIGN_KEY_CHECKS=1");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            cnxA.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");
            cnxB.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");
            generarDatosJugador(cnxA, CSV_JUG_A);
            generarDatosJugador(cnxB, CSV_JUG_B);
            generarDatosClasificacion(cnxA, CSV_CLA_A);
            generarDatosClasificacion(cnxB, CSV_CLA_B);
            generarDatosPremio(cnxA, CSV_PRE_A);
            generarDatosPremio(cnxB, CSV_PRE_B);
            generarDatosOpta(cnxA);
            generarDatosOpta(cnxB);
            cnxA.createStatement().execute("SET FOREIGN_KEY_CHECKS=1");
            cnxB.createStatement().execute("SET FOREIGN_KEY_CHECKS=1");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}