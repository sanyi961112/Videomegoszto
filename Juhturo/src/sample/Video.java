package sample;

import java.sql.Blob;
import java.sql.Date;

public class Video {
    private Integer video_id;
    private String cim;
    private String leiras;
    private Date feltoltes_datuma;
    private String feltolto;
    private String forras_fajl;
    private String kategoria;



    public Video(Integer video_id, String cim, String leiras, Date feltoltes_datuma, String feltolto, String forras_fajl, String kategoria) {
        this.video_id = video_id;
        this.cim = cim;
        this.leiras = leiras;
        this.feltoltes_datuma = feltoltes_datuma;
        this.feltolto = feltolto;
        this.forras_fajl = forras_fajl;
        this.kategoria = kategoria;

    }

    public Integer getVideo_id() {
        return video_id;
    }

    public String getCim() {
        return cim;
    }

    public String getLeiras() {
        return leiras;
    }

    public Date getFeltoltes_datuma() {
        return feltoltes_datuma;
    }

    public String getFeltolto() {
        return feltolto;
    }

    public String getForras_fajl() {
        return forras_fajl;
    }
    public String getKategoria() {
        return kategoria;
    }
}
