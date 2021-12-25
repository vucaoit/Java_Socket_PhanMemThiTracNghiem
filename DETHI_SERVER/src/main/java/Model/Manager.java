package Model;

import java.util.HashMap;

public class Manager {
    public static HashMap<String,String> OTP = new HashMap<>();// truyền vào <email,OTP>
    public static HashMap<String, Long> OTP_TIME = new HashMap<>();//truyền cái mã <OTP,thời gian gửi>
    public static HashMap<String,Integer> userOnline= new HashMap<String, Integer>();
}
