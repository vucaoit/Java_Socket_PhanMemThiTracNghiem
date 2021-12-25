package View;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controller.Manager;
import Model.ModelUser;
import com.google.gson.JsonSyntaxException;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class LoginView extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldEmail;
    private JPasswordField passwordFieldPassword;
    private JButton btnNewButtonLogin, btnNewButtonDangky;

    public LoginView() {
        createView();//khởi tạo view
        if (Manager.socket == null) {
            JOptionPane.showMessageDialog(getContentPane(), "Server đang bảo trì");
            System.exit(0);
        }
        btnNewButtonLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                login();
            }
        });
        btnNewButtonDangky.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //chạy qua đăng kí
                RegisterView registerView = new RegisterView();
                registerView.setVisible(true);
                setVisible(false);
            }
        });
        setLocationRelativeTo(null);
    }

    private void login() {
        String email = textFieldEmail.getText();
        String pass = passwordFieldPassword.getText();
        if(!email.isEmpty() && !pass.isEmpty()){
            //đẩy email và password(hash) vào hashMap
            HashMap<String, String> map = new HashMap<>();
            map.put("email", email);
            map.put("password", Manager.MD5(pass));
            //chuyen hashmap chua email va password thanh dang json
            String data = Manager.gson.toJson(map);
            String respone = Manager.SendEndReceiveData("login", data, getContentPane());//gửi dữ liệu lên server và nhận kết quả từ server trả về response
            //chuyen respone dạng json ve UserModel
            try{
                ModelUser user = Manager.gson.fromJson(respone, ModelUser.class);
                if (!user.isNull()) {//neu user khac null (server check co user voi email password da gui
                    //thanh cong chuyen sang homeView
                    if (user.getStatus() == 1) {//==1 là chức năng login đang bị khóa
                        JOptionPane.showMessageDialog(contentPane, "Tài khoản đang bị khóa");
                    } else {
                        //User không bị khóa chức năng login thì chuyển qua homeview
                        HomeView home = new HomeView(user);
                        home.setVisible(true);
                        //tat LoginView lai
                        setVisible(false);
                    }
                } else {
                    //Email mật khẩu sai
                    JOptionPane.showMessageDialog(contentPane, "Sai tài khoản hoặc mật khẩu");
                }
            } catch (JsonSyntaxException e) {
//            e.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(),"Tài khoản này đang được đăng nhập ở nơi khác");
            } catch (HeadlessException e) {
                e.printStackTrace();
            }
        }
        else{
            JOptionPane.showMessageDialog(getContentPane(),"Email và password không được để trống");
        }
    }

    //khởi tạo view
    public void createView() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 260);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{49, 94, 217, 158, 0};
        gbl_contentPane.rowHeights = new int[]{237, 237, 237, 237, 237, 237, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JLabel lblNewLabel = new JLabel("LOGIN");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 33));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.gridwidth = 4;
        gbc_lblNewLabel.anchor = GridBagConstraints.BASELINE;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 0;
        contentPane.add(lblNewLabel, gbc_lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Email");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 1;
        gbc_lblNewLabel_1.gridy = 1;
        contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

        textFieldEmail = new JTextField();
        GridBagConstraints gbc_textFieldEmail = new GridBagConstraints();
        gbc_textFieldEmail.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldEmail.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldEmail.gridx = 2;
        gbc_textFieldEmail.gridy = 1;
        contentPane.add(textFieldEmail, gbc_textFieldEmail);
        textFieldEmail.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("Password");
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_2.gridx = 1;
        gbc_lblNewLabel_2.gridy = 2;
        contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);

        passwordFieldPassword = new JPasswordField();
        GridBagConstraints gbc_passwordFieldPassword = new GridBagConstraints();
        gbc_passwordFieldPassword.insets = new Insets(0, 0, 5, 5);
        gbc_passwordFieldPassword.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordFieldPassword.gridx = 2;
        gbc_passwordFieldPassword.gridy = 2;
        contentPane.add(passwordFieldPassword, gbc_passwordFieldPassword);

        btnNewButtonLogin = new JButton("\u0110\u0103ng nh\u00E2\u0323p");
        GridBagConstraints gbc_btnNewButtonLogin = new GridBagConstraints();
        gbc_btnNewButtonLogin.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButtonLogin.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButtonLogin.gridx = 2;
        gbc_btnNewButtonLogin.gridy = 3;
        contentPane.add(btnNewButtonLogin, gbc_btnNewButtonLogin);

        btnNewButtonDangky = new JButton("\u0110\u0103ng ky\u0301");
        GridBagConstraints gbc_btnNewButtonDangky = new GridBagConstraints();
        gbc_btnNewButtonDangky.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButtonDangky.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButtonDangky.gridx = 2;
        gbc_btnNewButtonDangky.gridy = 4;
        contentPane.add(btnNewButtonDangky, gbc_btnNewButtonDangky);

        textFieldEmail.setText("timafik865@kingsready.com");
        passwordFieldPassword.setText("123456");
    }
}
