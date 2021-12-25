package View;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.crypto.SecretKey;
import javax.swing.*;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.table.DefaultTableModel;

import Controller.Manager;
import Model.AES;
import Model.DataTransport;
import Model.ModelDeThi;
import Model.ModelUser;
import com.google.gson.reflect.TypeToken;

import java.awt.Font;
import java.awt.event.*;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.util.*;
import java.sql.Connection;
import java.sql.ResultSet;

public class HomeView extends JFrame {
    private JTable tableDanhSachDeThi;
    private HashMap<Integer, String> madeVaUser = new HashMap<>();//lưu danh sách đề của user tạo
    private JButton btnNewButtonThiNgay, btnNewButtonLogout, btnNewButtonThongKe, btnNewButtonQuanLyDeThi, btnNewButtonUpdateProfile;

    public HomeView(ModelUser user) {
        createView();//khởi tạo view
        loadDanhSachDeThi();//load danh sách đề thi vào table
        //kiểm tra xem user có đang bị khóa thi không
        if (user.getStatus() == 23 || user.getStatus() == 2) {
            //nếu bị khóa thi thì tắt nút thi
            btnNewButtonThiNgay.setEnabled(false);
            JOptionPane.showMessageDialog(getContentPane(), "Bạn đang bị khóa chức năng thi");
        }
        btnNewButtonLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //đăng xuất chạy ra login view
                Manager.SendEndReceiveData("logout", "logout", getContentPane());//logout để server giảm số lượng user online
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
                setVisible(false);
            }
        });
        btnNewButtonThiNgay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thiDeThi(user);
            }
        });
        btnNewButtonThongKe.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                xemThongKe(user);
            }
        });
        btnNewButtonQuanLyDeThi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //chạy qua QuanLyDeThiView
                QuanLyDeThiView qldt = new QuanLyDeThiView(user);//truyền vào user đề bên view kia có được email user và chuyền về home nếu user nhấn quay lại Home
                qldt.setVisible(true);
                setVisible(false);
            }
        });
        btnNewButtonUpdateProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateProfile updateProfile = new UpdateProfile(user);
                updateProfile.setVisible(true);
                setVisible(false);
            }
        });
        setLocationRelativeTo(null);
    }

    private void xemThongKe(ModelUser user) {
        try {
            int index = tableDanhSachDeThi.getSelectedRow();//lấy dòng hiện tại
            int made = (Integer) tableDanhSachDeThi.getValueAt(index, 0);//lấy giá trị mã đề
            String tende = (String) tableDanhSachDeThi.getValueAt(index, 1);//lấy giá trị tên đề
            ThongKeView thongke = new ThongKeView(made, tende, user);//mở THongKeView lên
            thongke.setVisible(true);
            setVisible(false);
        } catch (ArrayIndexOutOfBoundsException e2) {
            //e2.printStackTrace();
            JOptionPane.showMessageDialog(getContentPane(), "Vui lòng chọn đề thi");
        }
    }

    private void thiDeThi(ModelUser user) {
        try {
            int index = tableDanhSachDeThi.getSelectedRow();//lấy dòng đang chọn
            int made = (Integer) tableDanhSachDeThi.getValueAt(index, 0);//lấy ô đầu tiên của đòng đang chọn
            if (!madeVaUser.get(made).equals(user.getEmail())) {//kiểm tra xem có phải đề của user không
                //nếu không phải là đề của user thì cho thi
                ThiView thiView = new ThiView(user, made);
                thiView.setVisible(true);
                setVisible(false);
            } else
                JOptionPane.showMessageDialog(getContentPane(), "Bạn không được phép thi đề do bạn tạo");
        } catch (ArrayIndexOutOfBoundsException e2) {
            //e2.printStackTrace();
            JOptionPane.showMessageDialog(getContentPane(), "Vui lòng chọn đề thi");
        }
    }

    private void loadDanhSachDeThi() {
        ArrayList<ModelDeThi> arr = new ArrayList<>();
        //lay danh sach bo de tu server
        try {
            String data = "getAllDeThi";//để vào cho zui
            String response = Manager.SendEndReceiveData("getAllDeThi", data, getContentPane());//gửi và nhận response(Json-ArrayList) từ server
            //chuyen json ve arraylist<modelDeThi>
            arr = Manager.gson.fromJson(response, new TypeToken<ArrayList<ModelDeThi>>() {}.getType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //them danh sach bo de vao object array
        Object[][] tableData = new Object[arr.size()][2];
        int count = 0;
        for (ModelDeThi x : arr) {//x là một trong những phần tử của arr, x sẽ được lấy liên tục cho đến khi nào hết mảng
            madeVaUser.put(x.getMade(), x.getEmail_tao());
            //madeVaUser là hashmap dùng để lưu dữ liệu mã đề và email tạo đề đó, để có gì tý thi check xem của phải là đề của user đang đăng nhập không
            tableData[count][0] = x.getMade();//cho vào dòng i cột đầu tiên
            tableData[count][1] = x.getTende();//cho vào dòng i cột thứ 2
            count++;
        }
        tableDanhSachDeThi.setModel(new DefaultTableModel(tableData, new String[]{//đưa object(tabledata) bode vào table danh sách đề mình tạo
                "Mã bộ đề", "Tên bộ đề"//ma bo de, ten bo de
        }
        ) {
            Class[] columnTypes = new Class[]{
                    String.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            boolean[] columnEditables = new boolean[]{
                    false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        tableDanhSachDeThi.getColumnModel().getColumn(0).setResizable(false);
        tableDanhSachDeThi.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableDanhSachDeThi.getColumnModel().getColumn(0).setMaxWidth(100);
    }

    public void createView() {
        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1176, 685);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{977, 10, 0};
        gridBagLayout.rowHeights = new int[]{646, 646, 646, 646, 646, 646, 646, 646, 646, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
        getContentPane().setLayout(gridBagLayout);

        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.gridheight = 9;
        gbc_panel.insets = new Insets(0, 0, 0, 5);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 0;
        getContentPane().add(panel, gbc_panel);
        panel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);
        tableDanhSachDeThi = new JTable();
        tableDanhSachDeThi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(tableDanhSachDeThi);

        JLabel lblNewLabel = new JLabel("Danh sách đề thi");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
        panel.add(lblNewLabel, BorderLayout.NORTH);

        JPanel panel_1 = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.gridheight = 9;
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 1;
        gbc_panel_1.gridy = 0;
        getContentPane().add(panel_1, gbc_panel_1);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[]{89, 0};
        gbl_panel_1.rowHeights = new int[]{23, 23, 23, 23, 23, 23, 23, 0};
        gbl_panel_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panel_1.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
        panel_1.setLayout(gbl_panel_1);

        btnNewButtonThiNgay = new JButton("Thi ngay");
        btnNewButtonThiNgay.setFont(new Font("Tahoma", Font.BOLD, 15));
        GridBagConstraints gbc_btnNewButtonThiNgay = new GridBagConstraints();
        gbc_btnNewButtonThiNgay.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButtonThiNgay.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewButtonThiNgay.gridx = 0;
        gbc_btnNewButtonThiNgay.gridy = 0;
        panel_1.add(btnNewButtonThiNgay, gbc_btnNewButtonThiNgay);

        btnNewButtonThongKe = new JButton("Xem thống kê");
        btnNewButtonThongKe.setFont(new Font("Tahoma", Font.BOLD, 15));
        GridBagConstraints gbc_btnNewButtonThongKe = new GridBagConstraints();
        gbc_btnNewButtonThongKe.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButtonThongKe.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewButtonThongKe.gridx = 0;
        gbc_btnNewButtonThongKe.gridy = 1;
        panel_1.add(btnNewButtonThongKe, gbc_btnNewButtonThongKe);

        btnNewButtonQuanLyDeThi = new JButton("Quản lý đề thi");
        btnNewButtonQuanLyDeThi.setFont(new Font("Tahoma", Font.BOLD, 15));
        GridBagConstraints gbc_btnNewButtonQuanLyDeThi = new GridBagConstraints();
        gbc_btnNewButtonQuanLyDeThi.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButtonQuanLyDeThi.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewButtonQuanLyDeThi.gridx = 0;
        gbc_btnNewButtonQuanLyDeThi.gridy = 2;
        panel_1.add(btnNewButtonQuanLyDeThi, gbc_btnNewButtonQuanLyDeThi);

        btnNewButtonUpdateProfile = new JButton("Cập nhật tài khoản");
        btnNewButtonUpdateProfile.setFont(new Font("Tahoma", Font.BOLD, 13));
        GridBagConstraints gbc_btnNewButtonUpdateProfile = new GridBagConstraints();
        gbc_btnNewButtonUpdateProfile.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButtonUpdateProfile.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewButtonUpdateProfile.gridx = 0;
        gbc_btnNewButtonUpdateProfile.gridy = 3;
        panel_1.add(btnNewButtonUpdateProfile, gbc_btnNewButtonUpdateProfile);

        btnNewButtonLogout = new JButton("Đăng xuất");
        btnNewButtonLogout.setFont(new Font("Tahoma", Font.BOLD, 15));
        GridBagConstraints gbc_btnNewButtonLogout = new GridBagConstraints();
        gbc_btnNewButtonLogout.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButtonLogout.gridx = 0;
        gbc_btnNewButtonLogout.gridy = 6;
        panel_1.add(btnNewButtonLogout, gbc_btnNewButtonLogout);
    }
}
