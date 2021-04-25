package sample;


import oracle.jdbc.pool.OracleDataSource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DB {

    private ResultSet resultSet;
    private Statement statement;
    private OracleDataSource ods;
    private String user = "sanyi";
    private String pass = "oracle";

    public static Integer parseInt(String s) {
        Integer i = null;
        try{
            i = Integer.parseInt(s);
        } catch (NumberFormatException n) {

        }
        return i;
    }
    //constructor
    public DB() {
        try{
            ods = new OracleDataSource();
            Class.forName("oracle.jdbc.OracleDriver");
            ods.setURL("jdbc:oracle:thin@localhost:1521:xe");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //String fh_nev, String jelszo, String teljes_nev, String email
    // ezzel minden adatot kinyerek egy tablabol array-be
    public ArrayList<Felhasznalok> read(){
        ArrayList<Felhasznalok> felhasznalok = new ArrayList<Felhasznalok>();
        try{
            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "select fh_nev, jelszo, teljes_nev, email from felhasznalok";
            resultSet = statement.executeQuery( sql );
            while (resultSet.next()){
                Felhasznalok f = new Felhasznalok(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
                felhasznalok.add(f);
                System.out.println(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return felhasznalok;
    }

    //register new felhasznalo
    public boolean insert(Felhasznalok f){
        try{
            //hashelt jelszo
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(f.getJelszo().getBytes(StandardCharsets.UTF_8));
            String hashedJelszo = new String(encodedHash);
            //insert
            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "INSERT INTO FELHASZNALOK (FH_NEV, JELSZO, TELJES_NEV, EMAIL) values ("+f.getFh_nev()+", '"+hashedJelszo+"', '"+f.getTeljes_nev()+"', "+f.getEmail()+")";
            System.out.println(sql);
            resultSet = statement.executeQuery( sql );
        } catch ( Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    //adatot update-el (video viewshoz tuti kell ilyen :) )
    public boolean update(Felhasznalok f){
        try{
            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "update FELHASZNALOK set jelszo='"+f.getJelszo()+"', teljes_nev='"+f.getTeljes_nev()+"', email='"+f.getEmail()+"'where fh_nev=" + f.getFh_nev();
            System.out.println(sql);
            resultSet = statement.executeQuery( sql );
        } catch ( Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    //torol egy felhasznalot, nem tudom ez kell-e de akkor adatlapra, majd logout from app
    public boolean delete(Felhasznalok f){
        try{
            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "delete from felhasznalok where fh_nev = " + f.getFh_nev();
            resultSet = statement.executeQuery( sql );
        } catch ( Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}