package Controller;

import java.util.Date;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil
{
    public static boolean sendEmail(Session session, String toEmail, String
            subject, String body)
    {
        try
        {
            //tham khảo mạng
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("mail@gmail.com"));

            msg.setReplyTo(InternetAddress.parse("recipients@gmail.com"));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail, false));

            //System.out.println("Message is ready");
            Transport.send(msg);

            //System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}