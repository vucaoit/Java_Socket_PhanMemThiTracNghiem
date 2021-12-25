package Model;

import java.util.ArrayList;

public class ModelDeThi {
private int made=0;
private String tende="";
private String email_tao="";
private int thoigianthi=0;
private ArrayList<ModelCauHoi> cauHoiArrayList = new ArrayList<>();

    public ModelDeThi() {
    }

    public ModelDeThi(int made, String tende, String email_tao, int thoigianthi, ArrayList<ModelCauHoi> cauHoiArrayList) {
        this.made = made;
        this.tende = tende;
        this.email_tao = email_tao;
        this.thoigianthi = thoigianthi;
        this.cauHoiArrayList = cauHoiArrayList;
    }

    public int getMade() {
        return made;
    }

    public void setMade(int made) {
        this.made = made;
    }

    public String getTende() {
        return tende;
    }

    public void setTende(String tende) {
        this.tende = tende;
    }

    public String getEmail_tao() {
        return email_tao;
    }

    public void setEmail_tao(String email_tao) {
        this.email_tao = email_tao;
    }

    public int getThoigianthi() {
        return thoigianthi;
    }

    public void setThoigianthi(int thoigianthi) {
        this.thoigianthi = thoigianthi;
    }

    public ArrayList<ModelCauHoi> getCauHoiArrayList() {
        return cauHoiArrayList;
    }

    public void setCauHoiArrayList(ArrayList<ModelCauHoi> cauHoiArrayList) {
        this.cauHoiArrayList = cauHoiArrayList;
    }
}
