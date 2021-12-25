package View;

import Controller.Manager;
import Model.*;
import com.google.gson.reflect.TypeToken;
//4
import java.awt.*;

import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import javax.swing.table.DefaultTableModel;

public class ThiView extends JFrame {

    private JPanel contentPane, panel;
    private int Time = 0;
    private float diem = 0;
    private int socauhoanthanh = 0;
    private Timer timer;
    private int Time_down = 0;
    private ArrayList<ModelCauHoi> arrcauhoi = new ArrayList<>();
    private JLabel lblNewLabel_tende, lblNewLabel_diem, lblNewLabel_Thoigain, lblNewLabel_socauhoanthanh;
    private ModelUser user;
    private int made;
    private JButton btnNewButton_Home;

    public ThiView(ModelUser user, int made) {
        this.user = user;
        this.made = made;
        createView();//khởi tạo view

        //lấy danh sách câu hỏi của đề thi
        ModelDeThi dethi = new ModelDeThi();
        try {
            String data = made + "";
            String response = Manager.SendEndReceiveData("laydethi", data, getContentPane());
            //chuyen json ve arraylist<modelDeThi>
            dethi = Manager.gson.fromJson(response, ModelDeThi.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        arrcauhoi = dethi.getCauHoiArrayList();//lấy toàn bộ câu hỏi trong đề thi
        swapArrayCauHoi();//đổi thứ tự câu và đáp án

        //setup thông tin thi
        lblNewLabel_tende.setText("Tên đề : " + dethi.getTende());
        lblNewLabel_diem.setText(diem + "");
        lblNewLabel_Thoigain.setText("60:00");
        int tongcauhoi = arrcauhoi.size();
        lblNewLabel_socauhoanthanh.setText(socauhoanthanh + "/" + tongcauhoi);
        float diem1cau = (float) 10.0 / tongcauhoi;//chia đều 10 điểm cho tổng số câu trong đề thi
        int thoigianthi = dethi.getThoigianthi() * 60;//thời gian đang là phút nên phải nhân thêm 60 đề chuyển về giây

        //Bộ đếm thời gian thi
        Time_down = thoigianthi;//set time down là thời gian thi của bộ đề
        timer = new Timer(1000, ex -> {//delay 1 giây
            if (Time_down > 0) {
                Time_down--;
                int phut = Time_down / 60;
                int giay = Time_down % 60;
                lblNewLabel_Thoigain.setText(phut + ":" + giay);//set lại thời gian sau 1 giây theo format mm:ss
            } else {
                ((Timer) (ex.getSource())).stop();
                JOptionPane.showMessageDialog(contentPane, "Hết Giờ");
                //sau khi đã hết giờ trước khi hoàn thành bài thi thì gửi thông tin thi lên server
                ThemThongTinThi(user, made, thoigianthi);
            }
        });
        timer.setInitialDelay(0);
        timer.start();//bắt đầu đếm ngược thời gian khi bắt đầu vào form thi

        //tạo ra danh sách các câu hỏi vào view
        for (int i = 0; i < arrcauhoi.size(); i++) {
            String cauhoi = arrcauhoi.get(i).getCauhoi();
            String dapan1 = arrcauhoi.get(i).getDapan1();
            String dapan2 = arrcauhoi.get(i).getDapan2();
            String dapan3 = arrcauhoi.get(i).getDapan3();
            String dapan4 = arrcauhoi.get(i).getDapan4();
            JPanel panel1 = new JPanel();
            ButtonGroup group = new ButtonGroup();
            JLabel lblNewLabel = new JLabel();
            createPanelCauHoi(i, panel1, group, lblNewLabel, cauhoi, dapan1, dapan2, dapan3, dapan4);
            JButton btnNewButton = new JButton("submit");
            //xử lý button submit khi check đáp án
            btnNewButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //gửi đáp án chọn lên server để check, khi nhấn submit
                    submitDapAn(cauhoi, made, group, lblNewLabel, diem1cau, tongcauhoi, btnNewButton, thoigianthi);
                }
            });
            panel1.add(btnNewButton);
            panel.add(panel1);
        }
        //Khi thoát ra mà nhấn No thì không có điều gì xảy ra
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(contentPane,
                        "Nếu bạn thoát, hệ thống sẽ lưu kết quả đang thi của bạn!\nBan có muốn thoát không ?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    //user thoát thì thêm thông tin thi hiện tại của user cho server cho dù user chưa thi xong hết.
                    guiThongTinThi(thoigianthi);//gửi thông tin thi lên server
                    System.exit(0);//thoát chương trình
                }
            }
        });
        btnNewButton_Home.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //trở về Home
                int reply = JOptionPane.showConfirmDialog(null, "Nếu bạn thoát, kết quả hiện tại sẽ được lưu trên hệ thống.\nBan có muốn thoát không ?", "WARNING", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    //nếu user chọn yes thì sẽ quay lại Home và đẩy kết quả hiện tại của user đang thi lên serrver cho dù chưa hoàn thành xong bài thi
                    guiThongTinThi(thoigianthi);//gửi thông tin thi lên server
                    timer.stop();
                    HomeView homeView = new HomeView(user);
                    homeView.setVisible(true);//quay lại Home
                    setVisible(false);
                } else {
                    //nhấn no thì không làm gì cả
                }
            }
        });
        setLocationRelativeTo(null);
    }

    private void guiThongTinThi(int thoigianthi) {
        ModelThongTinThi thoiGianThi = new ModelThongTinThi();
        thoiGianThi.setDiemThi(diem);
        thoiGianThi.setEmailThi(user.getEmail());
        thoiGianThi.setMade(made);
        thoiGianThi.setThoiGianHoanThanh(thoigianthi - Time_down);
        //gửi kết quả thi lên server
        String response = Manager.SendEndReceiveData("hoanthanhbaithi", Manager.gson.toJson(thoiGianThi), getContentPane());
    }

    private void createPanelCauHoi(int i, JPanel panel1, ButtonGroup group, JLabel lblNewLabel, String cauhoi, String dapan1, String dapan2, String dapan3, String dapan4) {

        panel1.setLayout(new GridLayout(0, 1, 0, 0));
        lblNewLabel.setText("Câu hỏi " + (i + 1) + " : " + cauhoi);//set tittle của câu hỏi
        panel1.add(lblNewLabel);

        JRadioButton rdbtnNewRadioButton = new JRadioButton(dapan1);//set tittle của dapan
        rdbtnNewRadioButton.setActionCommand(dapan1);//set value của đáp án
        panel1.add(rdbtnNewRadioButton);

        JRadioButton rdbtnNewRadioButton_1 = new JRadioButton(dapan2);//set tittle của dapan
        rdbtnNewRadioButton_1.setActionCommand(dapan2);//set value của đáp án
        panel1.add(rdbtnNewRadioButton_1);

        JRadioButton rdbtnNewRadioButton_2 = new JRadioButton(dapan3);//set tittle của dapan
        rdbtnNewRadioButton_2.setActionCommand(dapan3);//set value của đáp án
        panel1.add(rdbtnNewRadioButton_2);

        JRadioButton rdbtnNewRadioButton_3 = new JRadioButton(dapan4);//set tittle của dapan
        rdbtnNewRadioButton_3.setActionCommand(dapan4);//set value của đáp án
        panel1.add(rdbtnNewRadioButton_3);

        //tạo group button để lấy đáp án mà user chọn
        //thêm 4 radiobutton đáp án vào group  button
        group.add(rdbtnNewRadioButton);
        group.add(rdbtnNewRadioButton_1);
        group.add(rdbtnNewRadioButton_2);
        group.add(rdbtnNewRadioButton_3);

    }

    private void submitDapAn(String cauhoi, int made, ButtonGroup group, JLabel lblNewLabel, float diem1cau, int tongcauhoi, JButton btnNewButton, int thoigianthi) {
        try {
            //đẩy thông tin(mã đề user thi, câu hỏi user chọn, câu trả lời của user) lên server
            HashMap<String, String> map = new HashMap<>();
            map.put("cauhoi", cauhoi);
            map.put("made", made + "");
            map.put("traloi", group.getSelection().getActionCommand());
            String data = Manager.gson.toJson(map);//chuyển map về json
            String response = Manager.SendEndReceiveData("checkdapan", data, getContentPane());//gửi thông tin câu trả lời và nhận kết quả đúng sai từ server
            if (response.equals("true")) {//đúng thì câu hỏi chuyển sang màu xanh dương
                lblNewLabel.setForeground(Color.BLUE);
                diem += diem1cau;//tăng điểm lên
                lblNewLabel_diem.setText(diem + "");//set title điểm lại
            } else {//sai thì câu hỏi đổi thành màu đỏ
                lblNewLabel.setForeground(Color.RED);
            }
            socauhoanthanh++;//tăng số câu hoàn thành mỗi khi submit
            lblNewLabel_socauhoanthanh.setText(socauhoanthanh + "/" + tongcauhoi);//setText lại so câu hoàn thành
            btnNewButton.setEnabled(false);//sau khi submit thì disable button submit
            if (socauhoanthanh == tongcauhoi) {//nếu hoàn thành hết thì dừng thời gian lại và thêm thông tin thi lên server
                timer.stop();
                ThemThongTinThi(user, made, thoigianthi);
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    private void createView() {
        setTitle("Thi");
        System.out.println(made);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1150, 775);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane);

        JPanel panel_1 = new JPanel();
        contentPane.add(panel_1, BorderLayout.NORTH);
        panel_1.setLayout(new GridLayout(0, 1, 0, 0));

        btnNewButton_Home = new JButton("Quay Lại Home");
        panel_1.add(btnNewButton_Home);

        lblNewLabel_tende = new JLabel("ten de thi");
        panel_1.add(lblNewLabel_tende);

        JPanel panel_2 = new JPanel();
        panel_1.add(panel_2);
        panel_2.setLayout(new GridLayout(1, 1, 0, 0));

        JLabel lblNewLabel_7 = new JLabel("Thời gian còn lại    ");
        lblNewLabel_7.setHorizontalAlignment(SwingConstants.RIGHT);
        panel_2.add(lblNewLabel_7);

        lblNewLabel_Thoigain = new JLabel("59:59");
        panel_2.add(lblNewLabel_Thoigain);

        JLabel lblNewLabel_3 = new JLabel("Điểm   ");
        lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);
        panel_2.add(lblNewLabel_3);

        lblNewLabel_diem = new JLabel("10");
        panel_2.add(lblNewLabel_diem);

        JLabel lblNewLabel_5 = new JLabel("Số câu hoàn thành   ");
        lblNewLabel_5.setHorizontalAlignment(SwingConstants.RIGHT);
        panel_2.add(lblNewLabel_5);

        lblNewLabel_socauhoanthanh = new JLabel("10/10");
        panel_2.add(lblNewLabel_socauhoanthanh);
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 0, 0));
        scrollPane.getViewport().setView(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    public void ThemThongTinThi(ModelUser user, int made, int thoigianthi) {//hàm này có chức năng là thêm thông tin thi của user lên server
        //thêm thông tin vào ModelThongTinThi
        ModelThongTinThi thoiGianThi = new ModelThongTinThi();
        thoiGianThi.setDiemThi(diem);
        thoiGianThi.setEmailThi(user.getEmail());
        thoiGianThi.setMade(made);
        thoiGianThi.setThoiGianHoanThanh(thoigianthi - Time_down);

        String data = Manager.gson.toJson(thoiGianThi);//chuyển thoigianthi về Json
        String response = Manager.SendEndReceiveData("hoanthanhbaithi", data, getContentPane());//gửi kết quả lên server và nhận về response
        ModelThongTinThi thongTinThi = Manager.gson.fromJson(response, ModelThongTinThi.class);//chuyển response(Json-ModelThongTinThi) về Object Thongtinthi
        int timefinish = thoigianthi - Time_down;
        if (thongTinThi.getDiemThi() == diem && thongTinThi.getThoiGianHoanThanh() == timefinish) {//nếu thongtinthi giống nhau tức là chưa thi đề này lần nào
            //nếu kết quả chưa có trong server thì thông báo kết quả mới thi
            JOptionPane.showMessageDialog(contentPane, "Bạn đã hoàn thành bài thi với thời gian là " + timefinish / 60 + " phút " + timefinish % 60 + " giây." +
                    "\nĐiểm số là " + diem + "\nXếp hạng " + thongTinThi.getRank());
            timer.stop();
            HomeView home = new HomeView(user);
            home.setVisible(true);
            setVisible(false);
        } else {
            //nếu kết quả đã có trên server thì thông báo user đã thi đề này và gửi kết quả thi lần đầu của user, và không cập nhật lại kết quả.
            JOptionPane.showMessageDialog(contentPane, "Bạn đã thi đề này trước đó với kết quả\n" +
                    "Điểm : " + thongTinThi.getDiemThi() + "\nThời gian hoàn thành : " + thongTinThi.getThoiGianHoanThanh() / 60 + " phút " + thongTinThi.getThoiGianHoanThanh() % 60 + " giây.\n" +
                    "Xếp hạng : " + thongTinThi.getRank());
            timer.stop();
            HomeView home = new HomeView(user);
            home.setVisible(true);
            setVisible(false);
        }
    }

    public void swapArrayCauHoi() {
        //Hàm này có chức năng tráo đổi thứ tự câu hỏi và thứ tự đáp án
        for (int i = 0; i < arrcauhoi.size(); i++) {
            Collections.swap(arrcauhoi, new Random().nextInt(arrcauhoi.size()), new Random().nextInt(arrcauhoi.size()));
        }
        for (ModelCauHoi x : arrcauhoi) {//tráo đổi đáp án của từng câu hỏi
            swapDapAn(x);
        }
    }

    public void swapDapAn(ModelCauHoi cauHoi) {
        //hàm này có chức năng tráo đổi vị trí 4 đáp án
        ArrayList<String> arr = new ArrayList<>();
        //thêm 4 câu hỏi vào arr
        arr.add(cauHoi.getDapan1());
        arr.add(cauHoi.getDapan2());
        arr.add(cauHoi.getDapan3());
        arr.add(cauHoi.getDapan4());
        Collections.shuffle(arr);//tráo đáp án
        cauHoi.setDapan1(arr.get(0));
        cauHoi.setDapan2(arr.get(1));
        cauHoi.setDapan3(arr.get(2));
        cauHoi.setDapan4(arr.get(3));
    }
}
