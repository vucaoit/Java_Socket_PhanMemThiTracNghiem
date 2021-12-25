package Model;

import java.util.ArrayList;

public class ModelThongKe {
    private int made=0;
    private String tende="";
    private int songuoilam=0;
    private float diemTB=0;
    private float diemCao=0;
    ArrayList<ModelThongTinThi> danhSachNguoiLam = new ArrayList<>();
//lấy thống kê thôi
    public ModelThongKe() {
    }

    public ModelThongKe(int made, String tende, int songuoilam, float diemTB, float diemCao, ArrayList<ModelThongTinThi> danhSachNguoiLam) {
        this.made = made;
        this.tende = tende;
        this.songuoilam = songuoilam;
        this.diemTB = diemTB;
        this.diemCao = diemCao;
        this.danhSachNguoiLam = danhSachNguoiLam;
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

    public int getSonguoilam() {
        return songuoilam;
    }

    public void setSonguoilam(int songuoilam) {
        this.songuoilam = songuoilam;
    }

    public float getDiemTB() {
        return diemTB;
    }

    public void setDiemTB(float diemTB) {
        this.diemTB = diemTB;
    }

    public float getDiemCao() {
        return diemCao;
    }

    public void setDiemCao(float diemCao) {
        this.diemCao = diemCao;
    }

    public ArrayList<ModelThongTinThi> getDanhSachNguoiLam() {
        return danhSachNguoiLam;
    }

    public void setDanhSachNguoiLam(ArrayList<ModelThongTinThi> danhSachNguoiLam) {
        this.danhSachNguoiLam = danhSachNguoiLam;
    }
}
