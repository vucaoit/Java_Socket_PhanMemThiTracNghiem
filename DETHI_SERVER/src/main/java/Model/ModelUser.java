package Model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class ModelUser {
    String email = null;
    String password = null;
    String hoTen =null;
    String gioiTinh = null;
    Date ngaySinh = null;
    int status =0;
    public ModelUser(){

    }
    public ModelUser(String email, String password, String hoTen, String gioiTinh, Date ngaySinh,int status) {
        this.email = email;
        this.status = status;
        this.password = password;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }
    public String getStringDate(){
        return  new SimpleDateFormat("yyyy/MM/dd").format(this.ngaySinh);
    }
    public void setEmail(String email) {
        this.email = email.trim();
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

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
    public boolean isNull(){
        if(ngaySinh == null)return true;
        if(email == null)return true;
        if(hoTen == null) return true;
        if(gioiTinh == null) return true;
        return false;
    }
}
