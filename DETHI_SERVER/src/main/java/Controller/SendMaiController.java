package Controller;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class SendMaiController {
    private static String email="hoclaptrinh.mail@gmail.com";
    private static String password = "dethiserver16";
    private static String email_nguoiNhan = "";
    private static String title = "DETHI OTP CODE";
    private static String OPTCode = "";
    public SendMaiController(String email_nguoiNhan,String OTP) {
        this.email_nguoiNhan = email_nguoiNhan;
        this.OPTCode = OTP;
    }
    public static boolean sendEmail(String toEmail,String otp){
        //tham khảo mạng
        //System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        //SSL
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email,password);
            }
        };
        Session session = Session.getInstance(props, auth);
        EmailUtil.sendEmail(session,toEmail,title, otp);
        return true;
    }
}
