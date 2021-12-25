package Controller;

import Model.AES;
import Model.DataTransport;
import com.google.gson.Gson;

import javax.crypto.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;

public class Manager {
    public static Socket socket =null;
    public static ObjectInputStream reader = null;//dùng để đọc object từ server
    public static ObjectOutputStream writer = null;//gửi object từ client
    public static Gson gson = new Gson();//chuyển object về json
    public static PublicKey publicKeyserver = null;//nhận public key từ server (duy nhất)
    private  static String host = "LOCALHOST";
    private static int port = 4949;
    private static AES aes = new AES();//Khởi tạo khóa AES của client
    public static boolean start(){
        //hàm này chạy khi mở chương trình lên và kết nối tới server
        //thực hiện quá trình trao đổi khóa
        try{
            socket = new Socket(host, port);//kết nối tới server
            System.out.println("connecting...");
            reader = new ObjectInputStream(socket.getInputStream());
            writer = new ObjectOutputStream(socket.getOutputStream());
            publicKeyserver = (PublicKey) reader.readObject();//nhận public key từ server
            String key = aes.getKetString();//lấy khóa AES của client
            String keyAesMaHoa = RSAEncrpytion(key,publicKeyserver);
            writer.writeObject(keyAesMaHoa);//gửi khóa AES(Secret key) cho server
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
         catch (IOException e) {
            System.out.println("Server chưa online");
            close();
            return false;
        }
        return true;
    }
    public static boolean close(){
        //close
        try{
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            //e.printStackTrace();
            return false;
        } catch (NullPointerException ex){
            return false;
        }
        return true;
    }
    public static String RSAEncrpytion(String message,PublicKey publicKey){
        // Mã hoá dữ liệu
        String strEncrypt = "";
        try{
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, publicKey);
            byte encryptOut[] = c.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encryptOut);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return strEncrypt;
    }
    public static String MD5(String md5) {
        //băm mật khẩu dạng MD5
        //hàm này tham khảo trên mạng
        md5 = md5+"detaiso4sgu";//thêm chuỗi đằng sau để chống bị dịch ngược md5
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
    public static String SendEndReceiveData(String funtion, String data, Container parentComponent){
        //thực hiện quá trình gửi->nhận dữ liệu từ server(gửi rồi mới nhận)
        String giaiMaDuLieuNhan="";
        try{
            DataTransport dataTransport = new DataTransport();//tạo DataTransport để chuyển dữ liệu qua server
            dataTransport.setData(aes.encrypt(data));
            dataTransport.setFunction(aes.encrypt(funtion));//hàm gửi qua server để server biết client đang thực hiện chức năng nào
            Manager.writer.writeObject(dataTransport); //Gửi đối tượng lên cho server
            dataTransport  = (DataTransport) Manager.reader.readObject();//Nhận kết quả từ server gửi về dạng object (DataTransport)
            String response = dataTransport.getData();//lấy data(json-đã mã hóa) từ server (json tùy thuộc vào chức năng gửi)
            System.out.println("FROM SERVER : "+response);//ghi kết quả ra console (có hay ko cũng đc)
            giaiMaDuLieuNhan = aes.decrypt(dataTransport.getData(),aes.getKey());//dùng key aes giải dữ liệu từ server
            return giaiMaDuLieuNhan;//trả kết quả (đã giải mã) dạng chuỗi cho hàm
        } catch (IOException | ClassNotFoundException e) {
           // e.printStackTrace();
            System.out.println("Server closed!!!");
            JOptionPane.showMessageDialog(parentComponent,"Server đang bảo trì");
            System.exit(0);
        }
        return giaiMaDuLieuNhan;//trả kết quả chuỗi cho hàm
    }
}
