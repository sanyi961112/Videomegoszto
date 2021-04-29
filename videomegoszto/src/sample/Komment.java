package sample;

import java.sql.Timestamp;

public class Komment {
    private String video_id;
    private String fh_nev;
    private Timestamp idopont;
    private String komment;

    public Komment(String video_id, String fh_nev, Timestamp idopont, String komment){
        this.video_id = video_id;
        this.fh_nev = fh_nev;
        this.idopont = idopont;
        this.komment = komment;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getFh_nev() {
        return fh_nev;
    }

    public Timestamp getIdopont() {
        return idopont;
    }

    public String getKomment() {
        return komment;
    }
}
