package Model;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModelUser {
    String email = null;
    String password = null;
    String hoTen =null;
    String gioiTinh = null;
    Date ngaySinh = null;
    int status = 0;
    //chứa user
    public ModelUser(){

    }
    public ModelUser(String email, String password, String hoTen, String gioiTinh, Date  ngaySinh, int status) {
        this.email = email;
        this.password = password;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.status = status;
    }
    public String getStringDate(){
        return  new SimpleDateFormat("yyyy/MM/dd").format(this.ngaySinh);
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date  getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date  ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
    public boolean isNull(){
        if(ngaySinh == null)return true;
        if(email == null)return true;
        if(hoTen == null) return true;
        if(gioiTinh == null) return true;
        return false;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ModelUser{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", ngaySinh=" + ngaySinh +
                '}';
    }
}
