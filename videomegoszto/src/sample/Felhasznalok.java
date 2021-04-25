package sample;

public class Felhasznalok {
    private String fh_nev;
    private String jelszo;
    private String teljes_nev;
    private String email;

    public Felhasznalok(String fh_nev, String jelszo, String teljes_nev, String email){
        this.fh_nev = fh_nev;
        this.jelszo = jelszo;
        this.teljes_nev = teljes_nev;
        this.email = email;
    }

    public String getFh_nev() {
        return fh_nev;
    }

//    public void setFh_nev(String fh_nev) {
//        this.fh_nev = fh_nev;
//    }

    public String getJelszo() {
        return jelszo;
    }

//    public void setJelszo(String jelszo) {
//        this.jelszo = jelszo;
//    }

    public String getTeljes_nev() {
        return teljes_nev;
    }

//    public void setTeljes_nev(String teljes_nev) {
//        this.teljes_nev = teljes_nev;
//    }

    public String getEmail() {
        return email;
    }

//    public void setEmail(String email) {
//        this.email = email;
//    }
}
