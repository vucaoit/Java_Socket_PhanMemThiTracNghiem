package SocketController;

import Controller.ControllerDB;
import Controller.SendMaiController;
import Model.*;
import com.google.gson.Gson;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class ThreadSocket extends Thread {
    private Socket socket = null;
    private ObjectOutputStream sendToClient = null;
    private ObjectInputStream fromClient = null;
    private RSA rsa = new RSA();//sinh ra khóa công khai và các hàm về xử lý mã hóa
    private Gson gson = new Gson();// gson dùng để chuyển object về json, thư viện Gson dùng maven để add vào project
    private SecretKey key = null;
    private AES aes = new AES();//mã hóa dữ liệu ở Server gửi về client
    private String dataJson;
    private ControllerDB db;
    private ModelUser user;

    public ThreadSocket(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            db = new ControllerDB();// kết nối database
            user = new ModelUser();// chứa dữ liệu user mà client gửi lên
            sendToClient = new ObjectOutputStream(socket.getOutputStream());// Tao output stream
            fromClient = new ObjectInputStream(socket.getInputStream());//Tao input stream
            guiPublicKeyVaNhanSecretKey();
            while (true) {
                //nhan du lieu tu client
                DataTransport dataClient = new DataTransport();
                try {
                    dataClient = (DataTransport) fromClient.readObject();// DataTransport từ client
                } catch (IOException e) {
                    break;
                }
                System.out.println("FROM CLIENT: " + dataClient.getData());
                //giải mã dữ liệu
                dataJson = aes.decrypt(dataClient.getData(), key);//data gui tu client
                String function = aes.decrypt(dataClient.getFunction(), key);//chức năng client muốn thực hiện
                //thực hiện chức năng yêu cầu của client
                switch (function) {
                    case "login":
                        login();
                        break;
                    case "logout":
                        //giảm số lượng user online
                        logout();
                        break;
                    case "sendMail":
                        sendMail();
                        break;
                    case "xacThucEmail":
                        xacThucEmail();
                        break;
                    case "updateProfile":
                        updateThongTinTaiKhoan();
                        break;
                    case "changePassword":
                        changePassword();
                        break;
                    case "getAllDeThi":
                        getAllDeThi();
                        break;
                    case "getDeThiUser":
                        getDeThiCuaUser();
                        break;
                    case "laydethi":
                        layDeThiCuaMaDe();
                        break;
                    case "checkdapan":
                        kiemTraDapAnGuiLenCuaClient();
                        break;
                    case "xoaDeThi":
                        xoaDeThi();
                        break;
                    case "updateDeThi":
                        updateDeThi();
                        break;
                    case "hoanthanhbaithi":
                        luuKetQuaThi();
                        break;
                    case "suaxoade"://kiểm tra đề có xóa hoặc sửa được không, nếu có người thi thì ko xóa sửa được
                        kiemTraKhaNangSuaHoacXoaCuaMotDeThi();
                        break;
                    case "themdethi":
                        themDeThi();
                        break;
                    case "xemthongke":
                        xemThongKeCuaDeThi();
                        break;
                    default:
                        break;
                }
            }
            sendToClient.close();
            fromClient.close();
            socket.close();
            Manager.userOnline.remove(user.getEmail());
        } catch (EOFException exx) {
        } catch (Exception e) {
            e.printStackTrace();
            Manager.userOnline.remove(user.getEmail());
        }
    }

    private void xemThongKeCuaDeThi() {
        ArrayList<ModelThongTinThi> thongTinThiArrayList = db.getDanhSachNguoiLamDe(Integer.parseInt(dataJson));
        SendDataToClient("xemthongke", gson.toJson(thongTinThiArrayList));
    }

    private void themDeThi() {
        ModelDeThi deThi1 = gson.fromJson(dataJson, ModelDeThi.class);
        if (db.ThemDeThi(deThi1)) {
            SendDataToClient("themdethi", "success");
        } else {
            SendDataToClient("themdethi", "fail");
        }
    }

    private void kiemTraKhaNangSuaHoacXoaCuaMotDeThi() {
        if (db.deThiChuaCoNguoiThi(Integer.parseInt(dataJson))) {
            SendDataToClient("suaxoade", "true");
        } else {
            SendDataToClient("suaxoade", "false");
        }
    }

    private void luuKetQuaThi() {
        ModelThongTinThi thongTinThi = gson.fromJson(dataJson, ModelThongTinThi.class);
        if (db.ThemThongTinThi(thongTinThi)) {
            thongTinThi.setRank(db.getRank(thongTinThi.getEmailThi(), thongTinThi.getMade()));//sau khi client gửi thông tin thi của user thì lấy rank của kết quả
            SendDataToClient("hoanthanhbaithi", gson.toJson(thongTinThi));
        } else {
            //trường hợp user đã thi rồi
            thongTinThi = db.getThongTinThi(thongTinThi.getEmailThi(), thongTinThi.getMade());
            SendDataToClient("hoanthanhbaithi", gson.toJson(thongTinThi));
        }
    }

    private void updateDeThi() {
        ModelDeThi deThi = gson.fromJson(dataJson, ModelDeThi.class);
        if (db.updateDeThi(deThi)) {
            SendDataToClient("updateDeThi", "success");
        } else {
            SendDataToClient("updateDeThi", "fail");
        }
    }

    private void xoaDeThi() {
        if (db.XoaDeThi(Integer.parseInt(dataJson))) {
            SendDataToClient("xoaDeThi", "success");
        } else {
            SendDataToClient("xoaDeThi", "fail");
        }
    }

    private void kiemTraDapAnGuiLenCuaClient() {
        //kiểm tra đáp án của user
        HashMap<String, String> map2 = gson.fromJson(dataJson, HashMap.class);
        boolean check = db.checkdapan(Integer.parseInt(map2.get("made")), map2.get("cauhoi"), map2.get("traloi"));
        SendDataToClient("checkdapan", check + "");//check true of false
    }

    private void layDeThiCuaMaDe() {
        //lấy đề thi theo mã đề thi
        //user chọn đề thi bằng mã đề
        ModelDeThi dethi12 = db.getDeThi(Integer.parseInt(dataJson));
        SendDataToClient("laydethi", gson.toJson(dethi12));//ma de thi arrraylist<modeldethi>
    }

    private void getDeThiCuaUser() {
        //lấy đề thi của user tạo
        ArrayList<ModelDeThi> ar1 = db.getDeThiUser(dataJson);
        SendDataToClient("getDeThiUser", gson.toJson(ar1));
    }

    private void getAllDeThi() {
        //lấy đề thi trong db trả về arraylist
        ArrayList<ModelDeThi> ar = db.getAllDeThi();
        SendDataToClient("getAllDeThi", gson.toJson(ar));
    }

    private void changePassword() {
        HashMap<String, String> passwordInfo = gson.fromJson(dataJson, HashMap.class);
        String email = passwordInfo.get("email");
        String oldpass = passwordInfo.get("oldPass");
        String newpass = passwordInfo.get("newPass");
        if (db.changePassword(email, oldpass, newpass)) SendDataToClient("changePassword", "success");
        else SendDataToClient("changePassword", "fail");
    }

    private void logout() {
        Manager.userOnline.remove(user.getEmail());
        SendDataToClient("logout", "success");
    }

    private void updateThongTinTaiKhoan() {
        ModelUser user1 = gson.fromJson(dataJson, ModelUser.class);
        if (db.updateProfile(user1)) SendDataToClient("updateProfile", "success");
        else SendDataToClient("updateProfile", "fail");
    }

    private void xacThucEmail() {
        HashMap<String, String> map1 = new HashMap<>();
        map1 = gson.fromJson(dataJson, HashMap.class);
        user = gson.fromJson(map1.get("user"), ModelUser.class);
        String getOtp = Manager.OTP.get(user.getEmail());// mã OTP của server theo email của user
        //vucaoit@gmail.com-> <vucaoit@gmail.com,568479>
        if (db.isEmailChuaSuDung(user.getEmail())) {
            if (map1.get("otp").equals(getOtp)) {//kiểm tra xem otp của user có giống của hệ thống không
                if (System.currentTimeMillis() - Manager.OTP_TIME.get(getOtp) <= 60 * 10 * 1000) {//nằm trong khoảng còn hạn
                    //lấy thời gian hiện tại trừ đi thời gian lúc gửi mail sẽ ra thời gian còn còn hạn của otp
                    // System.out.println(db.ThemUser(user));
                    db.ThemUser(user);
                    Manager.OTP.remove(user.getEmail());
                    Manager.OTP_TIME.remove(getOtp);
                    // remove hashmap có key là vucaoit@gmail.com
                    SendDataToClient("xacThucEmail", "success");
                } else {
                    SendDataToClient("xacThucEmail", "timeout");
                }
            } else {
                SendDataToClient("xacThucEmail", "fail");
            }
        } else {
            DataTransport dataTransfer = new DataTransport();
            SendDataToClient("xacThucEmail", "emailactived");
        }
    }

    private void sendMail() {
        user = gson.fromJson(dataJson, ModelUser.class);
        // System.out.println(user.toString());
        if (db.isEmailChuaSuDung(user.getEmail())) {
            //System.out.println("sucess");
            String otp="";
            while(true){
                otp = new DecimalFormat("000000").format(new Random().nextInt(999999));//sinh ra mã otp ngẫu nhiên
                if(Manager.OTP_TIME.get(otp)==null)break;
            }
            SendMaiController.sendEmail(user.getEmail(), otp);//gửi mail kèm mã otp cho user
            Manager.OTP.put(user.getEmail(), otp);//nhét mã otp vừa sinh ra vào hệ thống
            //<vucaoit@gmail.com,568479>
            Date date = new Date();
            Manager.OTP_TIME.put(otp, System.currentTimeMillis());//lấy thời gian hiện tại đưa vào hệ thống để check sự hết hạn của mã otp
            //<vucaoit@gmail.com,105489794564>
            SendDataToClient("sendMail", "success");
        } else {
            SendDataToClient("sendMail", "fail");
        }
    }

    private void login() {
        HashMap<String, String> map = gson.fromJson(dataJson, HashMap.class);
        for(String x : Manager.userOnline.keySet()) System.out.println(x);
        try {
            user = db.getUser(map.get("email"), map.get("password"));
            if (!user.isNull()){//có 2 trường hợp nếu không có trong db thì user sẽ là null, hoặc có thì trả về user ko null
                if (Manager.userOnline.get(map.get("email")) != null) {
                    if (Manager.userOnline.get(map.get("email")) == 1)
                        SendDataToClient("login", "fail");
                } else {
                    SendDataToClient("login", gson.toJson(user));
                }
                if(user.getStatus()!=1)Manager.userOnline.put(user.getEmail(), 1);
            }
            else SendDataToClient("login", gson.toJson(user));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void guiPublicKeyVaNhanSecretKey() {
        try {
            //gui publickey cho client
            sendToClient.writeObject(rsa.getPublicKey());
            //doc publickey tu client
            String keyAesMaHoa = (String) fromClient.readObject();//lấy key aes đã mã hóa của client
            String keyAesGiaiMa = rsa.Descrpytion(keyAesMaHoa, rsa.getPrivateKey());//giải mã key
            key = aes.StringtoSecretKey(keyAesGiaiMa);//chuyển chuỗi key đã đc giải mã về SecretKey
            //ket thuc gui nhan key
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void SendDataToClient(String function, String data) {
        //hàm này có chức năng gửi dữ liệu về client
        DataTransport dataTransfer = new DataTransport();
        dataTransfer.setFunction(aes.encrypt(function, key));
        dataTransfer.setData(aes.encrypt(data, key));
        try {
            sendToClient.writeObject(dataTransfer);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
