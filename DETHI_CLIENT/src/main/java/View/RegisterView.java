package View;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


import Controller.Manager;
import Controller.ValidInput;
import Model.ModelUser;
import com.toedter.calendar.JTextFieldDateEditor;
import com.toedter.calendar.JDateChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RegisterView extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldEmail;
    private JTextField textFieldHoten;
    private JLabel lblNewLabel_TimeDown;
    private JPasswordField passwordFieldMatkhau1;
    private JPasswordField passwordFieldMatkhau2;
    private JComboBox comboBoxGioitinh;
    private JTextFieldDateEditor dateEditor;
    private JTextField textFieldOTP;
    private ModelUser user;
    private JButton btnNewButtonDangKy, btnNewButtonXacThuc, btnNewButtonComeBackLogin;
    private int Time_down = 60 * 10;
    private Timer timer;

    public RegisterView() {
        createView();//khởi tạo view
        //chỗ này dùng để chạy thời gian hết hạn mã OTP, lúc này chưa chạy, khi nào start timer thì mới chạy
        timer = new Timer(1000, ex -> {
            if (Time_down > 0) {
                Time_down--;
                int phut = Time_down / 60;
                int giay = Time_down % 60;
                lblNewLabel_TimeDown.setText(phut + ":" + giay);
                if (Time_down == 60 * 10) ((Timer) (ex.getSource())).stop();
            } else {
                ((Timer) (ex.getSource())).stop();
                JOptionPane.showMessageDialog(contentPane, "Hết Giờ");
            }
        });
        timer.setInitialDelay(0);

        btnNewButtonDangKy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dangKy();
            }
        });
        btnNewButtonXacThuc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                xacThucOTP();
            }
        });
        btnNewButtonComeBackLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //không muốn đăng ký nữa thì về login
                timer.stop();//Đóng timer lại
                LoginView login = new LoginView();
                login.setVisible(true);
                setVisible(false);
            }
        });
        setLocationRelativeTo(null);
    }

    private void xacThucOTP() {
        if (textFieldOTP.getText().trim().isEmpty()) {//kiem tra input co empty khong
            JOptionPane.showMessageDialog(contentPane, "Nhập mã OTP");
        } else {
            String otp = textFieldOTP.getText().trim();
            HashMap<String, String> map = new HashMap<>();
            //lấy mã otp và email user cho vào hashmap để gửi qua server xác thực
            map.put("otp", otp);
            map.put("user", Manager.gson.toJson(user));
            String data = Manager.gson.toJson(map);
            String response = Manager.SendEndReceiveData("xacThucEmail", data, getContentPane());
            switch (response) {
                case "success":
                    JOptionPane.showMessageDialog(contentPane, "Đăng ký thành công.");
                    timer.stop();//Đóng timer lại
                    //quay lai form dang nhap
                    LoginView loginView = new LoginView();
                    loginView.setVisible(true);
                    setVisible(false);//dong form dang ky
                    break;
                case "fail":
                    JOptionPane.showMessageDialog(contentPane, "Mã xác thực sai.");
                    break;
                case "emailactived":
                    JOptionPane.showMessageDialog(contentPane, "Email đã được kích hoạt, đăng ký thất bại");
                    break;
                default:
                    JOptionPane.showMessageDialog(contentPane, "Mã xác thực đã hết hạn");
                    timer.stop();//Đóng timer lại
                    lblNewLabel_TimeDown.setText("0:00");//chỉnh labal time về lại 0:00
                    break;
            }
        }
    }

    private void dangKy() {
        String txtEmail = textFieldEmail.getText().trim();
        String txtHoTen = textFieldHoten.getText().trim();
        String pass1 = passwordFieldMatkhau1.getText().trim();
        String pass2 = passwordFieldMatkhau2.getText().trim();
        String ngaySinh = dateEditor.getText();

        if (!txtEmail.equals("") && !txtHoTen.equals("") && !pass1.equals("") && !pass2.equals("") && !ngaySinh.equals("")) {// kiểm tra xem form điền hết chưa
            if (ValidInput.isValidEmail(txtEmail)) {//kiểm tra email có đúng định dạng hay không email@gmail.com
                if (txtHoTen.length() > 3) {
                    if (ValidInput.isValidPassword(passwordFieldMatkhau1.getText())) {//kiểm tra passowrd có đúng định dạng không
                        if (pass1.equals(pass2)) {//kiểm tra pass1 và pass2 có khớp nhau không
                            try {
                                Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(ngaySinh);
                                if(ValidInput.isValidNgaySinh(date1)>1){
                                    user = new ModelUser();
                                    user.setNgaySinh(date1);
                                    user.setEmail(txtEmail);
                                    user.setHoTen(txtHoTen);
                                    user.setPassword(Manager.MD5(pass1));//chuyển password về MD5 rồi thêm vào user
                                    user.setGioiTinh(comboBoxGioitinh.getSelectedItem().toString());
                                    String data = Manager.gson.toJson(user);//chuyển user về json để gửi qua cho server
                                    String response = Manager.SendEndReceiveData("sendMail", data, getContentPane());//gửi data lên server và nhận về response
                                    if (response.equals("success")) {//đã gửi otp về email
                                        btnNewButtonXacThuc.setEnabled(true);//hiển thị nút xác thực
                                        Time_down = 60 * 10;//10 phút
                                        timer.restart();//chạy thời gian chờ kích hoạt OTP
                                        JOptionPane.showMessageDialog(contentPane, "Mã OTP đã được gửi về email của bạn.\nMã OTP sẽ hết hạn sau 10 phút.\nVui lòng xác thực bằng OTP.");
                                    } else {
                                        JOptionPane.showMessageDialog(contentPane, "Email đã được sử dụng, vui lòng chọn email khác");
                                    }
                                }
                                else{
                                    JOptionPane.showMessageDialog(contentPane, "Ngày sinh không hợp lệ");
                                }
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            JOptionPane.showMessageDialog(contentPane, "Mật khẩu không khớp");
                        }
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "Mật khẩu phải từ 6 ký tự và gồm chữ hoặc số hoặc chứa các ký tự !@#$%^&*");
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Họ tên phải lớn hơn 3 kí tự");
                }
            } else {
                JOptionPane.showMessageDialog(contentPane, "Email Không hợp lệ");
            }
        } else {
            JOptionPane.showMessageDialog(contentPane, "Vui lòng điền đầy đủ và đúng thông tin");
        }
    }

    //khởi tạo view
    public void createView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 578, 492);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{140, 0, 98, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JLabel lblNewLabel = new JLabel("\u0110\u0103ng ky\u0301 ta\u0300i khoa\u0309n");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.gridwidth = 4;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 0;
        contentPane.add(lblNewLabel, gbc_lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Email");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 0;
        gbc_lblNewLabel_1.gridy = 1;
        contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

        textFieldEmail = new JTextField();
        GridBagConstraints gbc_textFieldEmail = new GridBagConstraints();
        gbc_textFieldEmail.gridwidth = 2;
        gbc_textFieldEmail.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldEmail.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldEmail.gridx = 1;
        gbc_textFieldEmail.gridy = 1;
        contentPane.add(textFieldEmail, gbc_textFieldEmail);
        textFieldEmail.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("Ho\u0323 t\u00EAn");
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_2.gridx = 0;
        gbc_lblNewLabel_2.gridy = 2;
        contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);

        textFieldHoten = new JTextField();
        GridBagConstraints gbc_textFieldHoten = new GridBagConstraints();
        gbc_textFieldHoten.gridwidth = 2;
        gbc_textFieldHoten.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldHoten.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldHoten.gridx = 1;
        gbc_textFieldHoten.gridy = 2;
        contentPane.add(textFieldHoten, gbc_textFieldHoten);
        textFieldHoten.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("M\u00E2\u0323t kh\u00E2\u0309u");
        GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
        gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_3.gridx = 0;
        gbc_lblNewLabel_3.gridy = 3;
        contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);

        passwordFieldMatkhau1 = new JPasswordField();
        GridBagConstraints gbc_passwordFieldMatkhau1 = new GridBagConstraints();
        gbc_passwordFieldMatkhau1.gridwidth = 2;
        gbc_passwordFieldMatkhau1.insets = new Insets(0, 0, 5, 5);
        gbc_passwordFieldMatkhau1.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordFieldMatkhau1.gridx = 1;
        gbc_passwordFieldMatkhau1.gridy = 3;
        contentPane.add(passwordFieldMatkhau1, gbc_passwordFieldMatkhau1);

        JLabel lblNewLabel_4 = new JLabel("Nh\u00E2\u0323p la\u0323i m\u00E2\u0323t kh\u00E2\u0309u");
        GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
        gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_4.gridx = 0;
        gbc_lblNewLabel_4.gridy = 4;
        contentPane.add(lblNewLabel_4, gbc_lblNewLabel_4);

        passwordFieldMatkhau2 = new JPasswordField();
        GridBagConstraints gbc_passwordFieldMatkhau2 = new GridBagConstraints();
        gbc_passwordFieldMatkhau2.gridwidth = 2;
        gbc_passwordFieldMatkhau2.insets = new Insets(0, 0, 5, 5);
        gbc_passwordFieldMatkhau2.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordFieldMatkhau2.gridx = 1;
        gbc_passwordFieldMatkhau2.gridy = 4;
        contentPane.add(passwordFieldMatkhau2, gbc_passwordFieldMatkhau2);

        JLabel lblNewLabel_5 = new JLabel("Gi\u01A1\u0301i ti\u0301nh");
        GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
        gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_5.gridx = 0;
        gbc_lblNewLabel_5.gridy = 5;
        contentPane.add(lblNewLabel_5, gbc_lblNewLabel_5);

        comboBoxGioitinh = new JComboBox();
        comboBoxGioitinh.setModel(new DefaultComboBoxModel(new String[]{"Nam", "Nữ"}));
        comboBoxGioitinh.setSelectedIndex(0);
        GridBagConstraints gbc_comboBoxGioitinh = new GridBagConstraints();
        gbc_comboBoxGioitinh.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxGioitinh.insets = new Insets(0, 0, 5, 5);
        gbc_comboBoxGioitinh.gridx = 1;
        gbc_comboBoxGioitinh.gridy = 5;
        contentPane.add(comboBoxGioitinh, gbc_comboBoxGioitinh);

        JLabel lblNewLabel_6 = new JLabel("Nga\u0300y sinh");
        GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
        gbc_lblNewLabel_6.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_6.gridx = 0;
        gbc_lblNewLabel_6.gridy = 6;
        contentPane.add(lblNewLabel_6, gbc_lblNewLabel_6);

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.getCalendarButton().setFont(new Font("Tahoma", Font.PLAIN, 15));
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateEditor = (JTextFieldDateEditor) dateChooser.getDateEditor();
        dateEditor.setEditable(false);
        dateEditor.setBackground(Color.white);
        GridBagConstraints gbc_dateChooser = new GridBagConstraints();
        gbc_dateChooser.insets = new Insets(0, 0, 5, 5);
        gbc_dateChooser.fill = GridBagConstraints.BOTH;
        gbc_dateChooser.gridx = 1;
        gbc_dateChooser.gridy = 6;
        contentPane.add(dateChooser, gbc_dateChooser);

        JLabel lblNewLabel_7 = new JLabel("Nhập mã OTP");
        GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
        gbc_lblNewLabel_7.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_7.gridx = 0;
        gbc_lblNewLabel_7.gridy = 8;
        contentPane.add(lblNewLabel_7, gbc_lblNewLabel_7);

        textFieldOTP = new JTextField();
        textFieldOTP.setText("");
        GridBagConstraints gbc_textFieldOTP = new GridBagConstraints();
        gbc_textFieldOTP.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldOTP.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldOTP.gridx = 1;
        gbc_textFieldOTP.gridy = 8;
        contentPane.add(textFieldOTP, gbc_textFieldOTP);
        textFieldOTP.setColumns(10);

        btnNewButtonXacThuc = new JButton("Xác Thực");
        GridBagConstraints gbc_btnNewButtonXacThuc = new GridBagConstraints();
        gbc_btnNewButtonXacThuc.anchor = GridBagConstraints.WEST;
        gbc_btnNewButtonXacThuc.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButtonXacThuc.gridx = 2;
        gbc_btnNewButtonXacThuc.gridy = 8;
        contentPane.add(btnNewButtonXacThuc, gbc_btnNewButtonXacThuc);
        btnNewButtonXacThuc.setEnabled(false);
        btnNewButtonDangKy = new JButton("Đăng ký");
        btnNewButtonDangKy.setFont(new Font("Tahoma", Font.PLAIN, 17));

        GridBagConstraints gbc_btnNewButtonDangKy = new GridBagConstraints();
        gbc_btnNewButtonDangKy.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButtonDangKy.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButtonDangKy.gridx = 1;
        gbc_btnNewButtonDangKy.gridy = 7;
        contentPane.add(btnNewButtonDangKy, gbc_btnNewButtonDangKy);

        btnNewButtonComeBackLogin = new JButton("Đăng nhập");

        lblNewLabel_TimeDown = new JLabel("");
        GridBagConstraints gbc_lblNewLabel_TimeDown = new GridBagConstraints();
        gbc_lblNewLabel_TimeDown.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_TimeDown.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_TimeDown.gridx = 3;
        gbc_lblNewLabel_TimeDown.gridy = 8;
        contentPane.add(lblNewLabel_TimeDown, gbc_lblNewLabel_TimeDown);

        GridBagConstraints gbc_btnNewButtonComeBackLogin = new GridBagConstraints();
        gbc_btnNewButtonComeBackLogin.insets = new Insets(0, 0, 0, 5);
        gbc_btnNewButtonComeBackLogin.gridx = 0;
        gbc_btnNewButtonComeBackLogin.gridy = 9;
        contentPane.add(btnNewButtonComeBackLogin, gbc_btnNewButtonComeBackLogin);
    }
}
