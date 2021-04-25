package sample;

import java.sql.Blob;
import java.sql.Date;

public class Video {
    private String video_id;
    private String cim;
    private String leiras;
    private Date feltoltes_datuma;
    private String feltolto;
    private Blob forras_fajl;

    public Video (String video_id, String cim, String leiras, Date feltoltes_datuma, String feltolto, Blob forras_fajl){

    }

    public String getVideo_id() {
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

    public Blob getForras_fajl() {
        return forras_fajl;
    }

    public Integer getMegtekintes() {
        return megtekintes;
    }

    private Integer megtekintes;
}