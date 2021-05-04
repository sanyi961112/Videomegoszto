package sample;

public class Cimkek {
    private Integer video_id;
    private String cimke_nev;

    public Cimkek(Integer video_id, String cimke_nev){
        this.video_id = video_id;
        this.cimke_nev = cimke_nev;
    }

    public Integer getVideo_id() {
        return video_id;
    }

    public String getCimke_nev() {
        return cimke_nev;
    }
}
