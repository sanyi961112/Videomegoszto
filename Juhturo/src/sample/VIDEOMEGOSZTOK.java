package sample;

public class VIDEOMEGOSZTOK {
    private String fh_nev;
    private Integer FELTOLESEK_SZAMA;

    public VIDEOMEGOSZTOK(String fh_nev, Integer FELTOLESEK_SZAMA){
        this.fh_nev = fh_nev;
        this.FELTOLESEK_SZAMA = FELTOLESEK_SZAMA;
    }
    public String getFh_nev() {
        return fh_nev;
    }
    public Integer getFELTOLESEK_SZAMA(){
        return FELTOLESEK_SZAMA;
    }

}
