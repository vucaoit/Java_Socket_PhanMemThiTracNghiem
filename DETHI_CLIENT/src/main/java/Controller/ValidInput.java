package Controller;

import Model.ModelUser;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class ValidInput {
    public static boolean isValidEmail(String email){//kiểm tra tính hợp lệ của email
        String regex = "^([A-Za-z0-9._])+([@])+([A-Za-z0-9.-])+([\\.])+([A-Za-z]{2,6})$";//ten_email @ tên miền ex:email@gmail.com
        if(email.matches(regex))return true;//nếu đúng trả về true
        return false;
    }
    public static boolean isValidPassword(String password){//kiểm tra tính hợp lệ của password
        String regex = "^([a-zA-z0-9!@#$%^&*]{6,})+$";//chữ và số, độ dài >=6
        if(password.matches(regex))return true;//nếu khớp trả về true
        return false;
    }
    public static long isValidNgaySinh(Date ngaysinh){//kiểm tra tính hợp lệ của password
        long currentTime = System.currentTimeMillis() / (1000 * 60 * 60 * 24);
        long ngaySinhTime = ngaysinh.getTime() / (1000 * 60 * 60 * 24);
        return currentTime-ngaySinhTime;
    }
}
