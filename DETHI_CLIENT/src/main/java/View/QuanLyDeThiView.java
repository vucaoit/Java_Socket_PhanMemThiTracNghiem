package View;

import Controller.Manager;
import Model.*;
import com.google.gson.reflect.TypeToken;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class QuanLyDeThiView extends JFrame {

    private JPanel contentPane, panel, panel_1;
    private JLabel lblNewLabel, lblNewLabel_2, lblNewLabel_1;
    private JTextField textFieldTenDe;
    private JComboBox comboBoxThoiGian, comboBoxSoCau;
    private JButton btnNewButtonTao, btnNewButtonXoaDe, btnNewButtonThemDeThi, btnNewButtonUpdateDeThi, btnNewButtonXoaCau, btnNewButtonHome, btnNewButtonThemCau;
    private JScrollPane scrollPane, scrollPane_1;
    private JTable tableDanhSachDeThi, tableDanhSachCauHoi;
    private ArrayList<ModelDeThi> arr = new ArrayList<>();
    private String[] optionthoigian = new String[]{"15", "45", "60", "90", "120", "other"};
    private String[] optioncauhoi = new String[]{"10", "20", "40", "50", "100", "other"};
    private ModelUser user;

    public QuanLyDeThiView(ModelUser user) {
        this.user = user;
        createView();//khởi tạo view
        if (user.getStatus() == 3 || user.getStatus() == 23) {
            //kiểm tra xem có đang bị khóa chức năng tạo đề ko, nếu bị thì nút thêm bị tắt
            btnNewButtonThemDeThi.setEnabled(false);
            JOptionPane.showMessageDialog(contentPane, "Bạn đang bị khóa chức năng tạo đề");
        }
        loadDanhSachDeThi(user);//load lại danh sách đề thi của user vào table, hàm này ở cuối
        btnNewButtonTao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                taoBangCauHoiMau();
            }
        });
        btnNewButtonThemCau.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                themCauVaoTableCauHoi();//thêm 1 dòng trống vào table câu hỏi
            }
        });
        btnNewButtonXoaCau.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //xóa dòng câu hỏi đang chọn
                try{
                    ((DefaultTableModel) tableDanhSachCauHoi.getModel()).removeRow(tableDanhSachCauHoi.getSelectedRow());
                } catch (Exception exception) {
//                    exception.printStackTrace();
                    JOptionPane.showMessageDialog(getContentPane(),"Vui lòng chọn câu hỏi để xóa");
                }
            }
        });
        btnNewButtonThemDeThi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themDeThi();//thêm đề thi lên server
            }
        });
        btnNewButtonUpdateDeThi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateDeThi();//cập nhật lại đề thi nếu chưa có người thi đề đó
            }
        });
        btnNewButtonXoaDe.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                xoaDeThi();//xóa đề thi nếu chưa có người thi đề đó
            }
        });
        btnNewButtonHome.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //chạy về home lại
                HomeView home = new HomeView(user);
                home.setVisible(true);
                setVisible(false);
            }
        });
        tableDanhSachDeThi.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                chonDeThiTrongDanhSach(e);//chọn đề thi trong danh sách đề thi của user
            }
        });

        comboBoxSoCau.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themSoCauTrongCombobox();//Thêm option trong combobox số câu
            }
        });
        comboBoxThoiGian.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themThoiGianTrongCombobox();//Thêm option trong combobox thời gian
            }
        });
        setLocationRelativeTo(null);
    }

    private void themThoiGianTrongCombobox() {
        if (comboBoxThoiGian.getSelectedItem().toString().equals("other")) {
            String newSoCau = "";
            String regex = "^[0-9]+$";
            while (true) {
                newSoCau = JOptionPane.showInputDialog("Nhập Thời gian");
                if (newSoCau == null || newSoCau.matches(regex)) {
                    try {
                        int num = Integer.parseInt(newSoCau);
                        if (num != 0) {
                            int index = isExitsOption(comboBoxThoiGian, num + "");
                            if (index == -1) {//kiểm tra thời gian đã nhập có trong combobox chưa nếu có thì select, không thì thêm và gần cuối
                                comboBoxThoiGian.removeItemAt(comboBoxThoiGian.getItemCount() - 1);
                                comboBoxThoiGian.addItem(num);
                                comboBoxThoiGian.addItem("other");
                                comboBoxThoiGian.setSelectedIndex(comboBoxThoiGian.getItemCount() - 2);
                            } else comboBoxThoiGian.setSelectedIndex(index);
                            break;
                        } else {
                            JOptionPane.showMessageDialog(contentPane, "Thời gian phải lớn hơn 0");
                        }
                    } catch (NumberFormatException ee) {
                        //nhấn cancel thì chọn value đầu
                        comboBoxThoiGian.setSelectedIndex(0);
                        break;
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Thời gian không hợp lệ");
                }
            }
        }
    }

    private void themSoCauTrongCombobox() {
        //thêm giá trị ngoài khi user chọn orther và nhập một giá trị mới
        if (comboBoxSoCau.getSelectedItem().toString().equals("other")) {
            String newSoCau = "";
            String regex = "^[0-9]+$";//regex kiểm tra có phải là số không
            while (true) {//lặp cho tới khi nào cancel form hoặc nhập đúng dữ liệu số và khác 0
                newSoCau = JOptionPane.showInputDialog("Nhập số câu");
                if (newSoCau == null || newSoCau.matches(regex)) {
                    try {
                        int num = Integer.parseInt(newSoCau);
                        if (num != 0) {
                            int index = isExitsOption(comboBoxSoCau, num + "");//kiểm tra số câu nhập đã có trong combobox chưa
                            if (index == -1) {//nếu có thì thêm vào
                                comboBoxSoCau.removeItemAt(comboBoxSoCau.getItemCount() - 1);
                                comboBoxSoCau.addItem(num);
                                comboBoxSoCau.addItem("other");
                                comboBoxSoCau.setSelectedIndex(comboBoxSoCau.getItemCount() - 2);
                            } else
                                comboBoxSoCau.setSelectedIndex(index);//chưa có thì chọn tại trị trí giống giá trị nhập
                            break;
                        } else {
                            JOptionPane.showMessageDialog(contentPane, "Số câu phải lớn hơn 0");
                        }
                    } catch (NumberFormatException ee) {
                        //nhấn cancel thì chọn value đầu
                        comboBoxSoCau.setSelectedIndex(0);
                        break;
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Số câu không hợp lệ");
                }
            }
        }
    }

    private void chonDeThiTrongDanhSach(ListSelectionEvent e) {
        int indexOptionSoCau = -1;
        int indexOptionThoiGian = -1;
        int cauhoisize = 10;
        int thoigian = 15;
        //phương thức này thực hiện khi user click vào 1 dòng trong tableDanhSachDeThi
        if (e.getValueIsAdjusting()) {//dùng cái này để tránh hiện tượng click 2 lần khi chọn dòng trong table
            //vì khi click bị dính 2 lần sẽ tạo ra 2 option y hệt nhau
            for (ModelDeThi x : arr) {
                try {
                    if (x.getMade() == (Integer) tableDanhSachDeThi.getValueAt(tableDanhSachDeThi.getSelectedRow(), 0)) {
                        textFieldTenDe.setText(x.getTende());
                        ArrayList<ModelCauHoi> cauHois = x.getCauHoiArrayList();
                        //them option so cau hoi
                        indexOptionSoCau = isExitsOption(comboBoxSoCau, cauHois.size() + "");
                        cauhoisize = cauHois.size();
                        //them option thoi gian thi
                        indexOptionThoiGian = isExitsOption(comboBoxThoiGian, x.getThoigianthi() + "");
                        thoigian = x.getThoigianthi();
                        //set lại danh sách câu hỏi của mã đề đã chọn
                        DefaultTableModel dm = (DefaultTableModel) tableDanhSachCauHoi.getModel();
                        int rowCount = dm.getRowCount();
                        for (int i = rowCount - 1; i >= 0; i--) {//xóa danh sách câu hỏi trong table câu hỏi hiện tại
                            dm.removeRow(i);
                        }
                        //them danh sach câu hỏi mới của đề thi vao object array
                        for (ModelCauHoi a : cauHois) {
                            dm.addRow(new Object[]{a.getCauhoi(), a.getDapan1(), a.getDapan2(), a.getDapan3(), a.getDapan4(), a.getDapandung()});
                        }
                        //sửa lại column đáp án thành Jcombobox
                        setColumToCombobox();
                    }
                } catch (ArrayIndexOutOfBoundsException e1) {

                }
            }
            if (indexOptionSoCau == -1) {
                //nếu trong option chưa có giá trị của số câu đề thi thì thêm option đó vào
                comboBoxSoCau.setModel(new DefaultComboBoxModel(optioncauhoi));
                comboBoxSoCau.removeItemAt(comboBoxSoCau.getItemCount() - 1);
                comboBoxSoCau.addItem(cauhoisize);
                comboBoxSoCau.addItem("other");
                comboBoxSoCau.setSelectedIndex(comboBoxSoCau.getItemCount() - 2);
            } else comboBoxSoCau.setSelectedIndex(indexOptionSoCau);//chọn option có sẵn
            if (indexOptionThoiGian == -1) {
                //nếu trong option chưa có giá trị của thời đề thi thì thêm option đó vào
                comboBoxThoiGian.setModel(new DefaultComboBoxModel(optionthoigian));
                comboBoxThoiGian.removeItemAt(comboBoxThoiGian.getItemCount() - 1);
                comboBoxThoiGian.addItem(thoigian);
                comboBoxThoiGian.addItem("other");
                comboBoxThoiGian.setSelectedIndex(comboBoxThoiGian.getItemCount() - 2);
            } else {
                comboBoxThoiGian.setSelectedIndex(indexOptionThoiGian);//chọn option có sẵn
            }
        }
    }

    private void xoaDeThi() {
        try {
            //kiểm tra xem đề thi có ai thi chưa, có người thi thì ko cho xóa
            if (deKhongSuaDuoc(Integer.parseInt(tableDanhSachDeThi.getValueAt(tableDanhSachDeThi.getSelectedRow(), 0) + ""))) {
                String data = tableDanhSachDeThi.getValueAt(tableDanhSachDeThi.getSelectedRow(), 0) + "";//ma de
                String response = Manager.SendEndReceiveData("xoaDeThi", data, getContentPane());//gửi mã đề cần xóa qua cho server
                if (response.equals("success")) {//đã xóa
                    JOptionPane.showMessageDialog(contentPane, "Đã xóa bộ đề");
                    //xóa danh sách câu hỏi trong table trong view
                    DefaultTableModel dm = (DefaultTableModel) tableDanhSachCauHoi.getModel();
                    int rowCount = dm.getRowCount();
                    for (int i = rowCount - 1; i >= 0; i--) {
                        dm.removeRow(i);
                    }
                    textFieldTenDe.setText("");//làm trống ô tên đề thi
                    loadDanhSachDeThi(user);//load lại danh sách đề thi
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Xóa bộ đề thất bại");
                }
            } else JOptionPane.showMessageDialog(contentPane, "Đề đã có người thi không xóa được");
        } catch (ArrayIndexOutOfBoundsException er) {
            JOptionPane.showMessageDialog(contentPane, "Chưa chọn đề để xóa");
        }
    }

    private void updateDeThi() {
        try {
            if (deKhongSuaDuoc(Integer.parseInt(tableDanhSachDeThi.getValueAt(tableDanhSachDeThi.getSelectedRow(), 0) + ""))) {
                String tende = textFieldTenDe.getText().trim();
                if (!tende.equals("")) {
                    int socau = Integer.parseInt(comboBoxSoCau.getSelectedItem().toString());
                    ArrayList<ModelCauHoi> arcauhoi = new ArrayList<>();
                    int cauhoitablecount = tableDanhSachCauHoi.getRowCount();
                    if (socau == cauhoitablecount) {//nếu giá trị option số câu bằng tổng số câu trong table câu hỏi thì cho phép
                        int rownull = isFillAllTable();//kiểm tra xem có chỗ nào trong table câu hỏi chưa điền
                        if (rownull == -1) {//đã điền hết
                            int rowDapAnGiongNhau = dapAnTrungNhauTable();//kiểm tra đáp án có trùng nhau ko
                            if (rowDapAnGiongNhau == -1) {//ko trùng nhau
                                //thêm vào danh sách câu hỏi(arraylist cauhoi)
                                for (int i = 0; i < cauhoitablecount; i++) {
                                    ModelCauHoi ch = new ModelCauHoi();
                                    ch.setCauhoi((String) tableDanhSachCauHoi.getValueAt(i, 0));
                                    ch.setDapan1((String) tableDanhSachCauHoi.getValueAt(i, 1));
                                    ch.setDapan2((String) tableDanhSachCauHoi.getValueAt(i, 2));
                                    ch.setDapan3((String) tableDanhSachCauHoi.getValueAt(i, 3));
                                    ch.setDapan4((String) tableDanhSachCauHoi.getValueAt(i, 4));
                                    ch.setDapandung((String) tableDanhSachCauHoi.getValueAt(i, 5));
                                    arcauhoi.add(ch);
                                }
                                //tạo đề thi để chuẩn bị gửi cho server
                                ModelDeThi dethi = new ModelDeThi();
                                dethi.setMade(Integer.parseInt(tableDanhSachDeThi.getValueAt(tableDanhSachDeThi.getSelectedRow(), 0) + ""));
                                dethi.setThoigianthi(Integer.parseInt(comboBoxThoiGian.getSelectedItem().toString()));
                                dethi.setEmail_tao(user.getEmail());
                                dethi.setTende(textFieldTenDe.getText().trim());
                                dethi.setCauHoiArrayList(arcauhoi);//danh sách câu hỏi ở trên
                                String data = Manager.gson.toJson(dethi);//chuyển về json để gửi
                                String response = Manager.SendEndReceiveData("updateDeThi", data, getContentPane());//gửi và nhận kết quả từ server
                                if (response.equals("success")) {//thành công
                                    JOptionPane.showMessageDialog(contentPane, "Đã cập nhật bộ đề");
                                    //cập nhật xong thì xóa hết danh sách câu hỏi ở trong view
                                    DefaultTableModel dm = (DefaultTableModel) tableDanhSachCauHoi.getModel();
                                    int rowCount = dm.getRowCount();
                                    for (int i = rowCount - 1; i >= 0; i--) {
                                        dm.removeRow(i);
                                    }
                                    textFieldTenDe.setText("");//làm trống ô tên đề thi
                                    loadDanhSachDeThi(user);//rồi load lại danh sách đề thi
                                } else {
                                    JOptionPane.showMessageDialog(contentPane, "Cập nhật bộ đề thất bại");
                                }
                            } else {
                                JOptionPane.showMessageDialog(contentPane, "dòng " + rowDapAnGiongNhau + " trùng đáp án");
                            }
                        } else {
                            JOptionPane.showMessageDialog(contentPane, "dòng " + rownull + " chưa điền đủ");
                        }
                    } else {
                        if (socau > cauhoitablecount)
                            JOptionPane.showMessageDialog(contentPane, "Thiếu " + (socau - cauhoitablecount) + " Câu");
                        else
                            JOptionPane.showMessageDialog(contentPane, "Dư " + (cauhoitablecount - socau) + " Câu");
                    }
                } else JOptionPane.showMessageDialog(contentPane, "Tên đề thi chưa điền");
            } else JOptionPane.showMessageDialog(contentPane, "Đề đã có người thi, không cập nhật được");
        } catch (ArrayIndexOutOfBoundsException ar) {
            JOptionPane.showMessageDialog(contentPane, "Chưa chọn đề để cập nhật");
        }
    }

    private void themDeThi() {
        String tende = textFieldTenDe.getText().trim();
        if (!tende.equals("")) {
            int socau = Integer.parseInt(comboBoxSoCau.getSelectedItem().toString());//lấy ra số câu mà user chọn của comboboxSoCau
            ArrayList<ModelCauHoi> arcauhoi = new ArrayList<>();
            int cauhoitablecount = tableDanhSachCauHoi.getRowCount();//lấy số lượng dòng trong danh sách câu hỏi
            if (socau == cauhoitablecount) {//kiểm tra số lượng câu hỏi trong danh sách có bằng với số câu mà user chọn hay không
                int rowNull = isFillAllTable();//kiểm tra xem trong danh sách câu hỏi đã điển hết chưa, nếu điền hết thì trả về -1, nếu chưa thì trả về dòng chưa điền
                if (rowNull == -1) {
                    int rowDapAnGiongNhau = dapAnTrungNhauTable();//kiểm tra 4 đáp án có khác nhau không, nếu khác trả về -1, nếu không trả về câu đang có trùng đáp án
                    if (rowDapAnGiongNhau == -1) {
                        for (int i = 0; i < cauhoitablecount; i++) {//thêm câu hỏi trong table vào mảng
                            ModelCauHoi ch = new ModelCauHoi();
                            ch.setCauhoi((String) tableDanhSachCauHoi.getValueAt(i, 0));
                            ch.setDapan1((String) tableDanhSachCauHoi.getValueAt(i, 1));
                            ch.setDapan2((String) tableDanhSachCauHoi.getValueAt(i, 2));
                            ch.setDapan3((String) tableDanhSachCauHoi.getValueAt(i, 3));
                            ch.setDapan4((String) tableDanhSachCauHoi.getValueAt(i, 4));
                            ch.setDapandung((String) tableDanhSachCauHoi.getValueAt(i, 5));
                            arcauhoi.add(ch);
                        }
                        //tạo modelDeThi và thêm dữ liệu vào
                        ModelDeThi dethi = new ModelDeThi();
                        dethi.setThoigianthi(Integer.parseInt(comboBoxThoiGian.getSelectedItem().toString()));
                        dethi.setEmail_tao(user.getEmail());
                        dethi.setTende(textFieldTenDe.getText().trim());
                        dethi.setCauHoiArrayList(arcauhoi);

                        String data = Manager.gson.toJson(dethi);
                        String response = Manager.SendEndReceiveData("themdethi", data, getContentPane());//gửi đê thi lên server và nhận kết quả từ server.
                        if (response.equals("success")) {
                            JOptionPane.showMessageDialog(contentPane, "Đã thêm bộ đề");
                            DefaultTableModel dm = (DefaultTableModel) tableDanhSachCauHoi.getModel();
                            int rowCount = dm.getRowCount();
                            for (int i = rowCount - 1; i >= 0; i--) {
                                dm.removeRow(i);
                            }
                            textFieldTenDe.setText("");
                            loadDanhSachDeThi(user);//load lại table bộ đề
                        } else {
                            JOptionPane.showMessageDialog(contentPane, "Thêm bộ đề thất bại");
                        }
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "Dòng " + rowDapAnGiongNhau + " có đáp án trùng nhau");
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Dòng " + rowNull + " chưa điền hết");
                }
            } else {
                if (socau > cauhoitablecount)
                    JOptionPane.showMessageDialog(contentPane, "Thiếu " + (socau - cauhoitablecount) + " Câu");
                else JOptionPane.showMessageDialog(contentPane, "Dư " + (cauhoitablecount - socau) + " Câu");
            }
        } else JOptionPane.showMessageDialog(contentPane, "Tên đề thi chưa điền");

    }

    private void themCauVaoTableCauHoi() {
        //thêm câu mới vào cuối table câu hỏi
        DefaultTableModel model = (DefaultTableModel) tableDanhSachCauHoi.getModel();
        model.addRow(new Object[]{"", "", "", "", "", ""});
        //set lại cột đáp án đúng là combobox
        TableColumn sportColumn = tableDanhSachCauHoi.getColumnModel().getColumn(5);
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("1");
        comboBox.addItem("2");
        comboBox.addItem("3");
        comboBox.addItem("4");
        sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }

    private void taoBangCauHoiMau() {
        int socau = Integer.parseInt(comboBoxSoCau.getSelectedItem().toString());
        DefaultTableModel model = (DefaultTableModel) tableDanhSachCauHoi.getModel();
        int rowCount = model.getRowCount();
        //xoa sạch danh sách câu hỏi hiện có
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        for (int i = 0; i < socau; i++) {
            model.addRow(new Object[]{"", "", "", "", "", ""});
        }
        setColumToCombobox();
    }

    public void loadDanhSachDeThi(ModelUser user) {
        //lấy danh sách bộ đề thì server
        try {
            String data = user.getEmail();
            String response = Manager.SendEndReceiveData("getDeThiUser", data, getContentPane());//lấy danh sách đề thi mà user đã tạo
            //chuyen json ve arraylist<modelDeThi>
            arr = Manager.gson.fromJson(response, new TypeToken<ArrayList<ModelDeThi>>() {
            }.getType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //xóa danh sách bộ đề hiện có trong view và load lại
        DefaultTableModel dm = (DefaultTableModel) tableDanhSachDeThi.getModel();
        int rowCount = dm.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            dm.removeRow(i);
        }
        //them danh sach bo de vao object array
        for (ModelDeThi x : arr) {
            dm.addRow(new Object[]{x.getMade(), x.getTende()});
        }
        tableDanhSachDeThi.getColumnModel().getColumn(0).setMaxWidth(150);
    }

    public int isExitsOption(JComboBox jComboBox, String value) {//kiểm tra giá trị có trong combobox không
        for (int i = 0; i < jComboBox.getItemCount(); i++) {
            String temp = jComboBox.getItemAt(i) + "";
            if (temp.equals(value)) return i;
        }
        return -1;
    }

    public int isFillAllTable() {//kiểm tra table câu hỏi đã điền chưa, nếu chưa trả về vị trí dòng chưa điền, nếu rồi trả về -1
        for (int i = 0; i < tableDanhSachCauHoi.getRowCount(); i++) {
            if (tableDanhSachCauHoi.getValueAt(i, 0).toString().trim().equals("")) return i + 1;
            if (tableDanhSachCauHoi.getValueAt(i, 1).toString().trim().equals("")) return i + 1;
            if (tableDanhSachCauHoi.getValueAt(i, 2).toString().trim().equals("")) return i + 1;
            if (tableDanhSachCauHoi.getValueAt(i, 3).toString().trim().equals("")) return i + 1;
            if (tableDanhSachCauHoi.getValueAt(i, 4).toString().trim().equals("")) return i + 1;
            if (tableDanhSachCauHoi.getValueAt(i, 5).toString().trim().equals("")) return i + 1;
        }
        return -1;
    }

    public int dapAnTrungNhauTable() {
        //kiểm tra đáp án trùng nhau trong table câu hỏi, nếu có dòng trung nhau đáp án thì trả về vị trí dòng đó, nếu không có trùng thì trả về -1
        for (int i = 0; i < tableDanhSachCauHoi.getRowCount(); i++) {
            String a = tableDanhSachCauHoi.getValueAt(i, 1).toString().trim();
            String b = tableDanhSachCauHoi.getValueAt(i, 2).toString().trim();
            String c = tableDanhSachCauHoi.getValueAt(i, 3).toString().trim();
            String d = tableDanhSachCauHoi.getValueAt(i, 4).toString().trim();
            if (a.equals(b) || a.equals(c) || a.equals(d)) return i + 1;
            else {
                if (b.equals(c) || b.equals(d)) return i + 1;
                else {
                    if (c.equals(d)) return i + 1;
                }
            }
        }
        return -1;
    }

    public boolean deKhongSuaDuoc(int made) {
        //kiem tra đề này có sửa hoặc xóa được không
        String data = made + "";
        String response = Manager.SendEndReceiveData("suaxoade", data, getContentPane());
        if (response.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    public void setColumToCombobox() {
        TableColumn sportColumn = tableDanhSachCauHoi.getColumnModel().getColumn(5);
        //edit lại cột đấp án là combobox
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("1");
        comboBox.addItem("2");
        comboBox.addItem("3");
        comboBox.addItem("4");
        sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }

    private void createView() {
        setTitle("Quản lý đề thi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1213, 692);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{70, 575, 125, 62, 93, 89, 0, 0};
        gbl_contentPane.rowHeights = new int[]{248, 37, 304, 0, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 0.0, 1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.gridwidth = 7;
        gbc_panel.insets = new Insets(0, 0, 5, 0);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 0;
        contentPane.add(panel, gbc_panel);
        panel.setLayout(new BorderLayout(0, 0));

        scrollPane = new JScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);
        tableDanhSachDeThi = new JTable();
        tableDanhSachDeThi.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "Mã đề", "Tên đề"
                }
        ) {
            Class[] columnTypes = new Class[]{
                    Object.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }

            ;
        });
        scrollPane.setViewportView(tableDanhSachDeThi);
        lblNewLabel = new JLabel("Tên đề");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 1;
        contentPane.add(lblNewLabel, gbc_lblNewLabel);

        textFieldTenDe = new JTextField();
        GridBagConstraints gbc_textFieldTenDe = new GridBagConstraints();
        gbc_textFieldTenDe.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldTenDe.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldTenDe.gridx = 1;
        gbc_textFieldTenDe.gridy = 1;
        contentPane.add(textFieldTenDe, gbc_textFieldTenDe);
        textFieldTenDe.setColumns(10);

        lblNewLabel_1 = new JLabel("Thời gian (Phút)");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 2;
        gbc_lblNewLabel_1.gridy = 1;
        contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

        comboBoxThoiGian = new JComboBox();
        comboBoxThoiGian.setModel(new DefaultComboBoxModel(optionthoigian));
        GridBagConstraints gbc_comboBoxThoiGian = new GridBagConstraints();
        gbc_comboBoxThoiGian.insets = new Insets(0, 0, 5, 5);
        gbc_comboBoxThoiGian.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxThoiGian.gridx = 3;
        gbc_comboBoxThoiGian.gridy = 1;
        contentPane.add(comboBoxThoiGian, gbc_comboBoxThoiGian);

        lblNewLabel_2 = new JLabel("Số câu");
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_2.gridx = 4;
        gbc_lblNewLabel_2.gridy = 1;
        contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);

        comboBoxSoCau = new JComboBox();
        comboBoxSoCau.setModel(new DefaultComboBoxModel(optioncauhoi));
        GridBagConstraints gbc_comboBoxSoCau = new GridBagConstraints();
        gbc_comboBoxSoCau.insets = new Insets(0, 0, 5, 5);
        gbc_comboBoxSoCau.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxSoCau.gridx = 5;
        gbc_comboBoxSoCau.gridy = 1;
        contentPane.add(comboBoxSoCau, gbc_comboBoxSoCau);

        btnNewButtonTao = new JButton("Tạo");
        GridBagConstraints gbc_btnNewButtonTao = new GridBagConstraints();
        gbc_btnNewButtonTao.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewButtonTao.gridx = 6;
        gbc_btnNewButtonTao.gridy = 1;
        contentPane.add(btnNewButtonTao, gbc_btnNewButtonTao);

        panel_1 = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.gridwidth = 7;
        gbc_panel_1.insets = new Insets(0, 0, 5, 0);
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 2;
        contentPane.add(panel_1, gbc_panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));

        scrollPane_1 = new JScrollPane();
        panel_1.add(scrollPane_1, BorderLayout.CENTER);
        tableDanhSachCauHoi = new JTable();
        tableDanhSachCauHoi.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"C\u00E2u ho\u0309i", "\u0110a\u0301p a\u0301n 1", "\u0110a\u0301p a\u0301n 2", "\u0110a\u0301p a\u0301n 3", "\u0110a\u0301p a\u0301n 4", "\u0110a\u0301p a\u0301n \u0111u\u0301ng"
                }
        ) {
            Class[] columnTypes = new Class[]{
                    Object.class, Object.class, String.class, String.class, String.class, Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            boolean[] columnEditables = new boolean[]{
                    true, true, true, true, true, true
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        tableDanhSachCauHoi.getColumnModel().getColumn(5).setMaxWidth(120);
        scrollPane_1.setViewportView(tableDanhSachCauHoi);
        btnNewButtonHome = new JButton("Home");
        GridBagConstraints gbc_btnNewButtonHome = new GridBagConstraints();
        gbc_btnNewButtonHome.insets = new Insets(0, 0, 0, 5);
        gbc_btnNewButtonHome.gridx = 0;
        gbc_btnNewButtonHome.gridy = 3;
        contentPane.add(btnNewButtonHome, gbc_btnNewButtonHome);

        btnNewButtonXoaDe = new JButton("Xóa đề");
        GridBagConstraints gbc_btnNewButtonXoaDe = new GridBagConstraints();
        gbc_btnNewButtonXoaDe.insets = new Insets(0, 0, 0, 5);
        gbc_btnNewButtonXoaDe.gridx = 2;
        gbc_btnNewButtonXoaDe.gridy = 3;
        contentPane.add(btnNewButtonXoaDe, gbc_btnNewButtonXoaDe);

        btnNewButtonXoaCau = new JButton("Xóa câu");
        GridBagConstraints gbc_btnNewButtonXoaCau = new GridBagConstraints();
        gbc_btnNewButtonXoaCau.insets = new Insets(0, 0, 0, 5);
        gbc_btnNewButtonXoaCau.gridx = 3;
        gbc_btnNewButtonXoaCau.gridy = 3;
        contentPane.add(btnNewButtonXoaCau, gbc_btnNewButtonXoaCau);

        btnNewButtonThemCau = new JButton("Thêm câu");
        GridBagConstraints gbc_btnNewButtonThemCau = new GridBagConstraints();
        gbc_btnNewButtonThemCau.insets = new Insets(0, 0, 0, 5);
        gbc_btnNewButtonThemCau.gridx = 4;
        gbc_btnNewButtonThemCau.gridy = 3;
        contentPane.add(btnNewButtonThemCau, gbc_btnNewButtonThemCau);

        btnNewButtonUpdateDeThi = new JButton("Cập nhật đề thi");
        GridBagConstraints gbc_btnNewButtonUpdateDeThi = new GridBagConstraints();
        gbc_btnNewButtonUpdateDeThi.insets = new Insets(0, 0, 0, 5);
        gbc_btnNewButtonUpdateDeThi.gridx = 5;
        gbc_btnNewButtonUpdateDeThi.gridy = 3;
        contentPane.add(btnNewButtonUpdateDeThi, gbc_btnNewButtonUpdateDeThi);

        btnNewButtonThemDeThi = new JButton("Thêm đề thi");
        GridBagConstraints gbc_btnNewButtonThemDeThi = new GridBagConstraints();
        gbc_btnNewButtonThemDeThi.gridx = 6;
        gbc_btnNewButtonThemDeThi.gridy = 3;
        contentPane.add(btnNewButtonThemDeThi, gbc_btnNewButtonThemDeThi);
    }
}
