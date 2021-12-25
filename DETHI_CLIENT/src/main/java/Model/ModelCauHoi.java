package Model;

public class ModelCauHoi {
    private String cauhoi="";
    private String dapan1="";
    private String dapan2="";
    private String dapan3="";
    private String dapan4="";
    private String dapandung="";
//thằng này dùng để chứa câu hỏi
    public ModelCauHoi() {
    }

    public ModelCauHoi( String cauhoi, String dapan1, String dapan2, String dapan3, String dapan4, String dapandung) {
        this.cauhoi = cauhoi;
        this.dapan1 = dapan1;
        this.dapan2 = dapan2;
        this.dapan3 = dapan3;
        this.dapan4 = dapan4;
        this.dapandung = dapandung;
    }
    public String getCauhoi() {
        return cauhoi;
    }

    public void setCauhoi(String cauhoi) {
        this.cauhoi = cauhoi;
    }

    public String getDapan1() {
        return dapan1;
    }

    public void setDapan1(String dapan1) {
        this.dapan1 = dapan1;
    }

    public String getDapan2() {
        return dapan2;
    }

    public void setDapan2(String dapan2) {
        this.dapan2 = dapan2;
    }

    public String getDapan3() {
        return dapan3;
    }

    public void setDapan3(String dapan3) {
        this.dapan3 = dapan3;
    }

    public String getDapan4() {
        return dapan4;
    }

    public void setDapan4(String dapan4) {
        this.dapan4 = dapan4;
    }

    public String getDapandung() {
        return dapandung;
    }

    public void setDapandung(String dapandung) {
        this.dapandung = dapandung;
    }
}
