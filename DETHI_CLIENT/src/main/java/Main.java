import Controller.Manager;
import View.LoginView;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
                try {
                    Manager.start();//bắt đầu thực hiện kết nối đến server và thực hiện quá trình trao đổi key
                    LoginView frame = new LoginView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }
}
