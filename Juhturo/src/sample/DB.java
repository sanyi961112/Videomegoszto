package sample;


import oracle.jdbc.pool.OracleDataSource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DB extends Main{

    private ResultSet resultSet;
    private Statement statement;
    private OracleDataSource ods;
    private String user = "geri";
    private String pass = "123456";
    long time =System.currentTimeMillis();
    java.sql.Date d =new java.sql.Date(time);
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
            ods.setURL("jdbc:oracle:thin:@localhost:1521:xe");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
              //  System.out.println(f.getFh_nev()+" - "+f.getJelszo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return felhasznalok;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


    //register new felhasznalo
    public boolean insert(Felhasznalok f){
        try{
            //hashelt jelszo
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(f.getJelszo().getBytes(StandardCharsets.UTF_8));
            String hashedJelszo = bytesToHex(hashed);
//            System.out.println("hashed jelszo:" + hashedJelszo);
            //insert
            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "INSERT INTO FELHASZNALOK (FH_NEV, JELSZO, TELJES_NEV, EMAIL) values ('"+f.getFh_nev()+"', '"+hashedJelszo+"', '"+f.getTeljes_nev()+"', '"+f.getEmail()+"')";
//            System.out.println(sql);
            resultSet = statement.executeQuery( sql );
        } catch ( Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    //adatot update-el (video viewshoz tuti kell ilyen)
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
            String sql = "delete from felhasznalok where fh_nev = '" + f.getFh_nev()+"'";
            resultSet = statement.executeQuery( sql );
        } catch ( Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean insertkat(Kategoriak f){
        try{
            //insert
            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "INSERT INTO KATEGORIAK (KATEGORIA_NEV ) values ('"+f.getKategoria_nev()+"')";
            System.out.println(sql);
            resultSet = statement.executeQuery( sql );
        } catch ( Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public ArrayList<Kategoriak> comboread(){
        ArrayList<Kategoriak> kategoriaks = new ArrayList<Kategoriak>();
        try{
            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "select KATEGORIA_NEV from KATEGORIAK";
            resultSet = statement.executeQuery( sql );
            while (resultSet.next()){
                Kategoriak k = new Kategoriak(resultSet.getString(1));
                kategoriaks.add(k);
                //System.out.println(k.getKategoria_nev());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kategoriaks;
    }
    public boolean katdelete(Kategoriak k){
        try{
            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "delete from KATEGORIAK where KATEGORIA_NEV = '" + k.getKategoria_nev()+"'";
            resultSet = statement.executeQuery( sql );
        } catch ( Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
 public boolean insertvideo(Video v){
     try{
         //insert
         Connection conn = ods.getConnection(user, pass);
         statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
         String sql = "INSERT INTO VIDEO (CIM, FELTOLTES_DATUMA, FORRAS_FAJL, LEIRAS, FELTOLTO, KATEGORIA) VALUES ('"+v.getCim()+"', TO_DATE('"+ d +" 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), '"+v.getForras_fajl()+"', '"+v.getLeiras()+"', '"+v.getFeltolto()+"', '"+v.getKategoria()+"')";
         System.out.println(sql);
         resultSet = statement.executeQuery( sql );
     } catch ( Exception ex){
         ex.printStackTrace();
         return false;
     }
     return true;
 }
 public ArrayList<Video> videoread(){
     ArrayList<Video> videos = new ArrayList<>();
     try{
         Connection conn = ods.getConnection(user, pass);
         statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
         String sql = "select VIDEO_ID, CIM, LEIRAS, FELTOLTES_DATUMA, FELTOLTO, FORRAS_FAJL, KATEGORIA from VIDEO";
         resultSet = statement.executeQuery( sql );
         while (resultSet.next()){
             Video v = new Video(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getDate(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7));
             videos.add(v);

//             System.out.println(v.getCim());
         }
     } catch (Exception e) {
         e.printStackTrace();
     }
     return videos;
 }
    public boolean insertcimke(Cimkek c){
        try{
            //insert
            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "INSERT INTO CIMKEK (VIDEO_ID, CIMKE_NEV) VALUES ('"+c.getVideo_id().toString()+"', '"+c.getCimke_nev()+"')";
            System.out.println(sql);
            resultSet = statement.executeQuery( sql );
        } catch ( Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean viddelete(Video v){
        try{
            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "delete from VIDEO where CIM = '" + v.getCim()+"'";
            resultSet = statement.executeQuery( sql );
        } catch ( Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertKomment(Komment k){
        try{
            //insert komment
            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "INSERT INTO KOMMENTEK (VIDEO_ID, CIMKE_NEV) VALUES ('"+k.getVideo_id().toString()+"', '"+k.getFh_nev()+"', '"+k.getIdopont()+"', '"+k.getKomment()+"')";
            System.out.println(sql);
            resultSet = statement.executeQuery( sql );
        } catch ( Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean insertVideoMegosztok(VIDEOMEGOSZTOK v){
        try{

            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "INSERT INTO VIDEOMEGOSZTOK (FH_NEV, FELTOLESEK_SZAMA) VALUES ('"+v.getFh_nev()+"','"+1+"')";
            System.out.println(sql);
            resultSet = statement.executeQuery( sql );
        } catch ( Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<VIDEOMEGOSZTOK> readVideoMegosztok() {
        ArrayList<VIDEOMEGOSZTOK> videos = new ArrayList<>();
        try{
            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "select FH_NEV, FELTOLESEK_SZAMA from VIDEOMEGOSZTOK";
            resultSet = statement.executeQuery( sql );
            while (resultSet.next()){
                VIDEOMEGOSZTOK v = new VIDEOMEGOSZTOK(resultSet.getString(1), resultSet.getInt(2));
                videos.add(v);

//             System.out.println(v.getCim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videos;
    }
    public boolean videomegosztokupdate(VIDEOMEGOSZTOK videomegosztok){
        try{
            Connection conn = ods.getConnection(user, pass);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "UPDATE VIDEOMEGOSZTOK SET FELTOLESEK_SZAMA = "+(videomegosztok.getFELTOLESEK_SZAMA()+1);
            System.out.println(sql);
            resultSet = statement.executeQuery( sql );
        } catch ( Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public static final String Belepesszamlalo="SELECT COUNT(FELHASZNALO) FROM LOGON Where (SELECT FH_NEV FROM FELHASZNALOK where felhasznalok.fh_nev = logon.felhasznalo)=logon.felhasznalo AND LOGON.CSELEKMENY='belépett'";
    public static final String Kilepesszamlalo="SELECT COUNT(FELHASZNALO) FROM LOGON Where (SELECT FH_NEV FROM FELHASZNALOK where felhasznalok.fh_nev = logon.felhasznalo)=logon.felhasznalo AND LOGON.CSELEKMENY='kilépett'";
    public static final String NEVÖSSZEHASOLITO="SELECT COUNT(FELTOLTO) FROM VIDEO Where (SELECT TELJES_NEV FROM FELHASZNALOK where felhasznalok.teljes_nev= video.feltolto)=video.feltolto";
    public static final String Cusswordusers="SELECT FH_NEV FROM felhasznalok WHERE (SELECT FELTOLTO FROM video WHERE leiras LIKE 'CUSSWORD' or leiras LIKE 'OUTHERCUSSWORD') = felhasznalok.fh_nev";
    public static final String FURAEMAILVIDEOMEGOSZTOKKOZOTT="SELECT FH_NEV FROM felhasznalok WHERE (SELECT FH_NEV FROM VIDEOMEGOSZTOK WHERE FELHASZNALOK.EMAIL NoT IN '@') = felhasznalok.fh_nev";
    public static final String Osszesvideo="SELECT SUM(FELTOLESEK_SZAMA) FROM VIDEOMEGOSZTOK WHERE (SELECT FH_NEV FROM FELHASZNALOK ) = videomegosztok.fh_nev";

}
