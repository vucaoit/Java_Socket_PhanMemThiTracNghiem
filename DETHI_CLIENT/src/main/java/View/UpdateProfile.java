package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import Controller.Manager;
import Controller.ValidInput;
import Model.ModelUser;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class UpdateProfile extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldEmail, textField_HoTen;
    private JTextFieldDateEditor dateEditor;
    private JPasswordField passwordFieldOldPassword, passwordField_NewPassword1, passwordField_NewPassword2;
    private JButton btnNewButtonHome, btnNewButtonCapNhat, btnNewButtonChangePassword;
    private JComboBox comboBoxGioiTinh;
    private JDateChooser dateChooserNgaySinh;

    public UpdateProfile(ModelUser user) {
        createView();//khởi tạo view
        //set up textfied
        textFieldEmail.setText(user.getEmail());
        textField_HoTen.setText(user.getHoTen());
        int gioitinh = 0;
        if (user.getGioiTinh().toLowerCase().equals("nam")) gioitinh = 0;
        else gioitinh = 1;
        comboBoxGioiTinh.setSelectedIndex(gioitinh);
        dateChooserNgaySinh.setDate(user.getNgaySinh());
        btnNewButtonCapNhat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                capNhatThongTinUser(user);
            }
        });
        btnNewButtonChangePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                capNhatPassword(user);
            }
        });
        btnNewButtonHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomeView homeView = new HomeView(user);
                homeView.setVisible(true);
                setVisible(false);
            }
        });
        setLocationRelativeTo(null);
    }

    private void capNhatPassword(ModelUser user) {
        //láy ra mật khẩu cũ
        String oldPass = passwordFieldOldPassword.getText().trim();
        //láy ra mật khẩu mới
        String pass1 = passwordField_NewPassword1.getText().trim();
        //láy ra mật khẩu nhập lại
        String pass2 = passwordField_NewPassword2.getText().trim();
        if (!pass1.equals("") && !pass2.equals("") && !oldPass.equals("")) {// kiểm tra xem form điền hết chưa
            if (ValidInput.isValidPassword(oldPass)) {
                if (ValidInput.isValidPassword(pass1)) {//kiểm tra passowrd có đúng định dạng không
                    if (pass1.equals(pass2)) {//kiểm tra pass1 và pass2 có khớp nhau không
                        //tạo hashmap để gửi thông tin password lên server
                        HashMap<String, String> infoPassword = new HashMap<>();
                        infoPassword.put("email", user.getEmail());
                        infoPassword.put("oldPass", Manager.MD5(oldPass));
                        infoPassword.put("newPass", Manager.MD5(pass1));
                        String data = Manager.gson.toJson(infoPassword);//chuyển user về json để gửi qua cho server
                        String response = Manager.SendEndReceiveData("changePassword", data, getContentPane());//gửi data lên server và nhận về response
                        if (response.equals("success")) {//đã gửi otp về email
                            JOptionPane.showMessageDialog(contentPane, "Đã đổi mật khẩu");
                        } else {
                            JOptionPane.showMessageDialog(contentPane, "Mật khẩu cũ không đúng");
                        }
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "Mật khẩu không khớp");
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Mật khẩu phải từ 6 ký tự và gồm chữ hoặc số hoặc chứa các ký tự !@#$%^&*");
                }
            } else
                JOptionPane.showMessageDialog(contentPane, "Mật khẩu cũ không đúng định dạng từ 6 ký tự và gồm chữ hoặc số hoặc chứa các ký tự !@#$%^&*");
        } else {
            JOptionPane.showMessageDialog(contentPane, "Vui lòng điền đầy đủ và đúng thông tin");
        }
    }

    private void capNhatThongTinUser(ModelUser user) {
        String txtHoTen = textField_HoTen.getText().trim();
        String ngaySinh = dateEditor.getText();
        if (!txtHoTen.equals("") && !ngaySinh.equals("")) {// kiểm tra xem form điền hết chưa
            if (txtHoTen.length() > 3) {
                //tạo tạm ModelUser user1
                ModelUser user1 = new ModelUser();
                //set lại email,hoten,gioitinh,ngaysinh từ form
                user1.setEmail(user.getEmail());
                user1.setHoTen(txtHoTen);
                user1.setGioiTinh(comboBoxGioiTinh.getSelectedItem().toString());
                try {
                    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(ngaySinh);
                    if(ValidInput.isValidNgaySinh(date1)>1){
                        user1.setNgaySinh(date1);
                        String data = Manager.gson.toJson(user1);//chuyển user về json để gửi qua cho server
                        String response = Manager.SendEndReceiveData("updateProfile", data, getContentPane());//gửi data lên server và nhận về response
                        if (response.equals("success")) {//cập nhật thành công
                            //sau khi cập nhật thành công thì update lại ModelUser user từ thằng user1 tạm
                            user.setHoTen(user1.getHoTen());
                            user.setGioiTinh(user1.getGioiTinh());
                            user.setNgaySinh(user1.getNgaySinh());
                            JOptionPane.showMessageDialog(contentPane, "Đã cập nhật thông tin");
                        } else {
                            JOptionPane.showMessageDialog(contentPane, "Cập nhật thông tin không thành công");
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(contentPane, "Ngày sinh không hợp lệ");
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(contentPane, "Họ tên phải lớn hơn 3 kí tự");
            }
        } else {
            JOptionPane.showMessageDialog(contentPane, "Vui lòng điền đầy đủ và đúng thông tin");
        }
    }

    private void createView() {
        setTitle("Update Profile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 739, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{99, 90, 402, 0, 0};
        gbl_contentPane.rowHeights = new int[]{45, 31, 31, 31, 32, 31, 40, 31, 31, 31, 31, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        btnNewButtonHome = new JButton("Home");
        GridBagConstraints gbc_btnNewButtonHome = new GridBagConstraints();
        gbc_btnNewButtonHome.anchor = GridBagConstraints.WEST;
        gbc_btnNewButtonHome.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButtonHome.gridx = 0;
        gbc_btnNewButtonHome.gridy = 0;
        contentPane.add(btnNewButtonHome, gbc_btnNewButtonHome);

        JLabel lblNewLabel = new JLabel("Cập nhật tài khoản");
        lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 2;
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
        textFieldEmail.setEditable(false);
        GridBagConstraints gbc_textFieldEmail = new GridBagConstraints();
        gbc_textFieldEmail.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldEmail.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldEmail.gridx = 2;
        gbc_textFieldEmail.gridy = 1;
        contentPane.add(textFieldEmail, gbc_textFieldEmail);
        textFieldEmail.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("Họ tên");
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_2.gridx = 1;
        gbc_lblNewLabel_2.gridy = 2;
        contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);

        textField_HoTen = new JTextField();
        GridBagConstraints gbc_textField_HoTen = new GridBagConstraints();
        gbc_textField_HoTen.insets = new Insets(0, 0, 5, 5);
        gbc_textField_HoTen.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_HoTen.gridx = 2;
        gbc_textField_HoTen.gridy = 2;
        contentPane.add(textField_HoTen, gbc_textField_HoTen);
        textField_HoTen.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("Giới tính");
        GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
        gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_3.gridx = 1;
        gbc_lblNewLabel_3.gridy = 3;
        contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);

        comboBoxGioiTinh = new JComboBox();
        comboBoxGioiTinh.setModel(new DefaultComboBoxModel(new String[]{"Nam", "Nữ"}));
        comboBoxGioiTinh.setSelectedIndex(0);
        GridBagConstraints gbc_comboBoxGioiTinh = new GridBagConstraints();
        gbc_comboBoxGioiTinh.anchor = GridBagConstraints.WEST;
        gbc_comboBoxGioiTinh.insets = new Insets(0, 0, 5, 5);
        gbc_comboBoxGioiTinh.gridx = 2;
        gbc_comboBoxGioiTinh.gridy = 3;
        contentPane.add(comboBoxGioiTinh, gbc_comboBoxGioiTinh);

        JLabel lblNewLabel_4 = new JLabel("Ngày sinh");
        GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
        gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_4.gridx = 1;
        gbc_lblNewLabel_4.gridy = 4;
        contentPane.add(lblNewLabel_4, gbc_lblNewLabel_4);

        dateChooserNgaySinh = new JDateChooser();
        dateChooserNgaySinh.setDateFormatString("dd/MM/yyyy\r\n");
        dateEditor = (JTextFieldDateEditor) dateChooserNgaySinh.getDateEditor();
        dateEditor.setEditable(false);
        dateEditor.setBackground(Color.white);
        GridBagConstraints gbc_dateChooserNgaySinh = new GridBagConstraints();
        gbc_dateChooserNgaySinh.insets = new Insets(0, 0, 5, 5);
        gbc_dateChooserNgaySinh.fill = GridBagConstraints.BOTH;
        gbc_dateChooserNgaySinh.gridx = 2;
        gbc_dateChooserNgaySinh.gridy = 4;
        contentPane.add(dateChooserNgaySinh, gbc_dateChooserNgaySinh);

        btnNewButtonCapNhat = new JButton("Cập nhật");
        GridBagConstraints gbc_btnNewButtonCapNhat = new GridBagConstraints();
        gbc_btnNewButtonCapNhat.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButtonCapNhat.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButtonCapNhat.gridx = 2;
        gbc_btnNewButtonCapNhat.gridy = 5;
        contentPane.add(btnNewButtonCapNhat, gbc_btnNewButtonCapNhat);

        JLabel lblNewLabel_6 = new JLabel("Đổi mật khẩu");
        lblNewLabel_6.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
        GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
        gbc_lblNewLabel_6.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_6.gridx = 2;
        gbc_lblNewLabel_6.gridy = 6;
        contentPane.add(lblNewLabel_6, gbc_lblNewLabel_6);

        JLabel lblNewLabel_5 = new JLabel("Mật khẩu cũ");
        GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
        gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_5.gridx = 1;
        gbc_lblNewLabel_5.gridy = 7;
        contentPane.add(lblNewLabel_5, gbc_lblNewLabel_5);

        passwordFieldOldPassword = new JPasswordField();
        GridBagConstraints gbc_passwordFieldOldPassword = new GridBagConstraints();
        gbc_passwordFieldOldPassword.insets = new Insets(0, 0, 5, 5);
        gbc_passwordFieldOldPassword.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordFieldOldPassword.gridx = 2;
        gbc_passwordFieldOldPassword.gridy = 7;
        contentPane.add(passwordFieldOldPassword, gbc_passwordFieldOldPassword);

        JLabel lblNewLabel_7 = new JLabel("Mật khẩu mới");
        GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
        gbc_lblNewLabel_7.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_7.gridx = 1;
        gbc_lblNewLabel_7.gridy = 8;
        contentPane.add(lblNewLabel_7, gbc_lblNewLabel_7);

        passwordField_NewPassword1 = new JPasswordField();
        GridBagConstraints gbc_passwordField_NewPassword1 = new GridBagConstraints();
        gbc_passwordField_NewPassword1.insets = new Insets(0, 0, 5, 5);
        gbc_passwordField_NewPassword1.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordField_NewPassword1.gridx = 2;
        gbc_passwordField_NewPassword1.gridy = 8;
        contentPane.add(passwordField_NewPassword1, gbc_passwordField_NewPassword1);

        JLabel lblNewLabel_8 = new JLabel("Nhập lại mật khẩu mới");
        GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
        gbc_lblNewLabel_8.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_8.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_8.gridx = 1;
        gbc_lblNewLabel_8.gridy = 9;
        contentPane.add(lblNewLabel_8, gbc_lblNewLabel_8);

        passwordField_NewPassword2 = new JPasswordField();
        GridBagConstraints gbc_passwordField_NewPassword2 = new GridBagConstraints();
        gbc_passwordField_NewPassword2.insets = new Insets(0, 0, 5, 5);
        gbc_passwordField_NewPassword2.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordField_NewPassword2.gridx = 2;
        gbc_passwordField_NewPassword2.gridy = 9;
        contentPane.add(passwordField_NewPassword2, gbc_passwordField_NewPassword2);

        btnNewButtonChangePassword = new JButton("Đổi mật khẩu");
        GridBagConstraints gbc_btnNewButtonChangePassword = new GridBagConstraints();
        gbc_btnNewButtonChangePassword.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButtonChangePassword.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButtonChangePassword.gridx = 2;
        gbc_btnNewButtonChangePassword.gridy = 10;
        contentPane.add(btnNewButtonChangePassword, gbc_btnNewButtonChangePassword);
    }

}
