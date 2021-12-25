package View;

import Controller.Manager;
import Model.*;
import com.google.gson.reflect.TypeToken;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.crypto.SecretKey;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ThongKeView extends JFrame {

    private JPanel contentPane;
    private JTable table;
    private JScrollPane scrollPane;
    private JLabel lblNewLabel_made, lblNewLabel_tende, lblNewLabel_songuoilam, lblNewLabel_diemtrungbinh, lblNewLabel_diemcaonhat;
    private JButton btnNewButtonHome;
    private int tongnguoilam;
    private float sumdiem, max;

    public ThongKeView(int made, String tende, ModelUser user) {
        createView();//khởi tạo view
        loadDanhSachNguoiLamDe(made);//load danh sách người làm đề đang chọn và thêm vào table
        //setup lại thông tin của bộ đề (mã đề, tên đề, số người thi, điểm trung bình, điểm cao nhất)
        lblNewLabel_made.setText(made + "");
        lblNewLabel_tende.setText(tende);
        lblNewLabel_songuoilam.setText(tongnguoilam + "");
        lblNewLabel_diemtrungbinh.setText((sumdiem / tongnguoilam) + "");
        lblNewLabel_diemcaonhat.setText(max + "");

        btnNewButtonHome.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //quay lại home
                HomeView homeView = new HomeView(user);
                homeView.setVisible(true);
                setVisible(false);
            }
        });
        setLocationRelativeTo(null);
    }

    private void loadDanhSachNguoiLamDe(int made) {
        ArrayList<ModelThongTinThi> arr = new ArrayList<>();
        //lay danh sach bo de tu server
        try {
            String data = made + "";
            //gửi mã đề cần xem thống kê lên server và nhận kết quả(Json-ArrayList) trả về
            String response = Manager.SendEndReceiveData("xemthongke", data, getContentPane());
            //chuyen json ve arraylist<modelDeThi>
            arr = Manager.gson.fromJson(response, new TypeToken<ArrayList<ModelThongTinThi>>() {
            }.getType());// trả về danh sách người đã thi đề này
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //them danh sach bo de vao object array
        Object[][] tableData = new Object[arr.size()][4];
        int count = 0;
        tongnguoilam = arr.size();
        sumdiem = 0;
        max = 0;
        for (ModelThongTinThi x : arr) {
            sumdiem += x.getDiemThi();
            if (x.getDiemThi() > max) max = x.getDiemThi();
            tableData[count][0] = x.getEmailThi();
            tableData[count][1] = x.getHoten();
            tableData[count][2] = x.getDiemThi();
            tableData[count][3] = (x.getThoiGianHoanThanh() / 60) + "p" + (x.getThoiGianHoanThanh() % 60) + "s";
            count++;
        }
        table = new JTable();
        //thêm danh sách người đã thi(tabledata) vào table
        table.setModel(new DefaultTableModel(tableData,
                new String[]{
                        "Email", "Họ Tên", "Điểm", "Thời Gian Thi"
                }
        ) {
            boolean[] columnEditables = new boolean[]{
                    false, false, false,false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        table.getColumnModel().getColumn(2).setMaxWidth(75);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setMaxWidth(150);
        scrollPane.setViewportView(table);
    }

    private void createView() {
        setTitle("Xem thống kê đề thi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1166, 780);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 100, 23, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        btnNewButtonHome = new JButton("Home");
        btnNewButtonHome.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gbc_btnNewButtonHome = new GridBagConstraints();
        gbc_btnNewButtonHome.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButtonHome.gridx = 0;
        gbc_btnNewButtonHome.gridy = 0;
        contentPane.add(btnNewButtonHome, gbc_btnNewButtonHome);

        JLabel lblNewLabel_4 = new JLabel("Mã đề");
        GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
        gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_4.gridx = 1;
        gbc_lblNewLabel_4.gridy = 1;
        contentPane.add(lblNewLabel_4, gbc_lblNewLabel_4);

        lblNewLabel_made = new JLabel("001");
        GridBagConstraints gbc_lblNewLabel_made = new GridBagConstraints();
        gbc_lblNewLabel_made.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_made.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_made.gridx = 3;
        gbc_lblNewLabel_made.gridy = 1;
        contentPane.add(lblNewLabel_made, gbc_lblNewLabel_made);

        JLabel lblNewLabel = new JLabel("Tên đề thi");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 1;
        gbc_lblNewLabel.gridy = 2;
        contentPane.add(lblNewLabel, gbc_lblNewLabel);

        lblNewLabel_tende = new JLabel("Đề thi trung học phổ thông quốc gia");
        GridBagConstraints gbc_lblNewLabel_tende = new GridBagConstraints();
        gbc_lblNewLabel_tende.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_tende.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_tende.gridx = 3;
        gbc_lblNewLabel_tende.gridy = 2;
        contentPane.add(lblNewLabel_tende, gbc_lblNewLabel_tende);

        JLabel lblNewLabel_1 = new JLabel("Số người đã làm");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 1;
        gbc_lblNewLabel_1.gridy = 3;
        contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

        lblNewLabel_songuoilam = new JLabel("506");
        GridBagConstraints gbc_lblNewLabel_songuoilam = new GridBagConstraints();
        gbc_lblNewLabel_songuoilam.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_songuoilam.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_songuoilam.gridx = 3;
        gbc_lblNewLabel_songuoilam.gridy = 3;
        contentPane.add(lblNewLabel_songuoilam, gbc_lblNewLabel_songuoilam);

        JLabel lblNewLabel_2 = new JLabel("Điểm trung bình");
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_2.gridx = 1;
        gbc_lblNewLabel_2.gridy = 4;
        contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);

        lblNewLabel_diemtrungbinh = new JLabel("8.75");
        GridBagConstraints gbc_lblNewLabel_diemtrungbinh = new GridBagConstraints();
        gbc_lblNewLabel_diemtrungbinh.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_diemtrungbinh.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_diemtrungbinh.gridx = 3;
        gbc_lblNewLabel_diemtrungbinh.gridy = 4;
        contentPane.add(lblNewLabel_diemtrungbinh, gbc_lblNewLabel_diemtrungbinh);

        JLabel lblNewLabel_3 = new JLabel("Điểm cao nhất");
        GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
        gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_3.gridx = 1;
        gbc_lblNewLabel_3.gridy = 5;
        contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);

        lblNewLabel_diemcaonhat = new JLabel("10");
        GridBagConstraints gbc_lblNewLabel_diemcaonhat = new GridBagConstraints();
        gbc_lblNewLabel_diemcaonhat.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_diemcaonhat.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_diemcaonhat.gridx = 3;
        gbc_lblNewLabel_diemcaonhat.gridy = 5;
        contentPane.add(lblNewLabel_diemcaonhat, gbc_lblNewLabel_diemcaonhat);

        JLabel lblNewLabel_10 = new JLabel("Danh sách những người đã làm");
        lblNewLabel_10.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
        GridBagConstraints gbc_lblNewLabel_10 = new GridBagConstraints();
        gbc_lblNewLabel_10.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_10.gridwidth = 4;
        gbc_lblNewLabel_10.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel_10.gridx = 0;
        gbc_lblNewLabel_10.gridy = 6;
        contentPane.add(lblNewLabel_10, gbc_lblNewLabel_10);

        scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridwidth = 4;
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 7;
        contentPane.add(scrollPane, gbc_scrollPane);
    }

}
