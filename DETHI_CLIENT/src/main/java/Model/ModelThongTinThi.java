package Model;

public class ModelThongTinThi {
    private int made=0;
    private String emailThi="";
    private float diemThi=0;
    private int thoiGianHoanThanh=0;
    private String hoten="";
private int rank=0;
    public ModelThongTinThi() {
    }
//sau khi thi xong hoặc kết thúc thi thì nó sẽ được gửi lên cho server

    public ModelThongTinThi(int made, String emailThi, float diemThi, int thoiGianHoanThanh, String hoten, int rank) {
        this.made = made;
        this.emailThi = emailThi;
        this.diemThi = diemThi;
        this.thoiGianHoanThanh = thoiGianHoanThanh;
        this.hoten = hoten;
        this.rank = rank;
    }

    public int getMade() {
        return made;
    }

    public void setMade(int made) {
        this.made = made;
    }

    public String getEmailThi() {
        return emailThi;
    }

    public void setEmailThi(String emailThi) {
        this.emailThi = emailThi;
    }

    public float getDiemThi() {
        return diemThi;
    }

    public void setDiemThi(float diemThi) {
        this.diemThi = diemThi;
    }

    public int getThoiGianHoanThanh() {
        return thoiGianHoanThanh;
    }

    public void setThoiGianHoanThanh(int thoiGianHoanThanh) {
        this.thoiGianHoanThanh = thoiGianHoanThanh;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
