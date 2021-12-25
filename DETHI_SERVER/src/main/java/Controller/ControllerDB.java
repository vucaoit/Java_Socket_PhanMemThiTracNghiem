package Controller;

import Model.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ControllerDB {
    //user
    public boolean ThemUser(ModelUser user) {
        Connection conn = new DBConnection().getConnect();
        if (conn != null) {
            try {
                //105' OR 1=1
                //select * from user where email ='105' or 1=1
                PreparedStatement pstmt = conn.prepareStatement(" insert into users (email,password,ho_ten,gioi_tinh,ngay_sinh) values (?,?,?,?,?);");
                pstmt.setString(1, user.getEmail());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getHoTen());
                pstmt.setString(4, user.getGioiTinh());
                pstmt.setString(5, user.getStringDate());
                int i = pstmt.executeUpdate();
                if (i > 0) return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean isEmailChuaSuDung(String email) {
        //hàm này kiểm tra có tồn tại email này trong database hay ko
        Connection conn = new DBConnection().getConnect();
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(" select * from users where email = ?;");
                pstmt.setString(1, email);
                ResultSet results = pstmt.executeQuery();
                int count = 0;
                while (results.next()) {
                    count++;
                }
                if (count == 0) return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public ModelUser getUser(String email, String password) {
        Connection conn = new DBConnection().getConnect();
        ModelUser user = new ModelUser();
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(" select * from users where email = ? and password = ?;");
                pstmt.setString(1, email);
                pstmt.setString(2, password);
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    user.setEmail(results.getString("email"));
                    user.setHoTen(results.getString("ho_ten"));
                    user.setGioiTinh(results.getString("gioi_tinh"));
                    user.setStatus(results.getInt("status"));
                    Date dbSqlDate = results.getDate("ngay_sinh");
                    Date dbSqlDateConverted = new Date(dbSqlDate.getTime());
                    user.setNgaySinh(dbSqlDateConverted);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public boolean updateProfile(ModelUser user) {
        Connection conn = new DBConnection().getConnect();
        int row = 0;
        String query = ("UPDATE Users SET ho_ten=?,gioi_tinh=?,ngay_sinh=? where email=?;");
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getHoTen());
            pstmt.setString(2, user.getGioiTinh());
            pstmt.setString(3, user.getStringDate());
            pstmt.setString(4, user.getEmail());
            row = pstmt.executeUpdate();
            if (row > 0) return true;
        } catch (SQLException ex) {
            // Exception handling
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean changePassword(String email, String oldpass, String newpass) {
        Connection conn = new DBConnection().getConnect();
        int row = 0;
        String truePass = "";
        String query = ("Select password from users where email=?;");
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet results = pstmt.executeQuery();
            while (results.next()) {
                truePass = results.getString("password").trim();
            }
            if (oldpass.equals(truePass)) {
                query = ("update users set password=? where email=?;");
                try (PreparedStatement pstmt1 = conn.prepareStatement(query)) {
                    pstmt1.setString(1, newpass);
                    pstmt1.setString(2, email);
                    row = pstmt1.executeUpdate();
                    if (row > 0) return true;
                } catch (SQLException ex) {
                    // Exception handling
                    ex.printStackTrace();
                    return false;
                }
            }
        } catch (SQLException ex) {
            // Exception handling
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    public int getSoluongUser() {
        Connection conn = new DBConnection().getConnect();
        ModelUser user = new ModelUser();
        int count = 0;
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(" select * from users;");
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    count++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public boolean LockUser(String email, int option) {
        //lock user theo chức năng chọn
        int row = 0;
        Connection conn = new DBConnection().getConnect();
        String query = ("UPDATE USERS SET STATUS=? WHERE EMAIL=?");
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, option);
            pstmt.setString(2, email);
            row = pstmt.executeUpdate();
            if (row != 1) return false;
        } catch (SQLException ex) {
            // Exception handling
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    //dethi
    public boolean ThemDeThi(ModelDeThi boDe) {// Thêm bộ đề vào CSDL trả về mã bộ đề
        Connection conn = new DBConnection().getConnect();
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(" insert into bode (ten,thoi_gian_thi,email_tao) values (?,?,?);");
                pstmt.setString(1, boDe.getTende());
                pstmt.setInt(2, boDe.getThoigianthi());
                pstmt.setString(3, boDe.getEmail_tao());
                int i = pstmt.executeUpdate();
                int max = getMaxIDBoDe(boDe.getEmail_tao());
                for (ModelCauHoi x : boDe.getCauHoiArrayList()) {
                    pstmt = conn.prepareStatement(" insert into cau (ma_bd,cau_hoi,phuong_an_1,phuong_an_2,phuong_an_3,phuong_an_4,dap_an) values (?,?,?,?,?,?,?);");
                    pstmt.setInt(1, max);
                    pstmt.setString(2, x.getCauhoi());
                    pstmt.setString(3, x.getDapan1());
                    pstmt.setString(4, x.getDapan2());
                    pstmt.setString(5, x.getDapan3());
                    pstmt.setString(6, x.getDapan4());
                    pstmt.setString(7, x.getDapandung());
                    i = pstmt.executeUpdate();
                }
                if (i > 0) return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean XoaDeThi(int made) {
        Connection conn = new DBConnection().getConnect();
        int row = 0;
        String query = ("delete from cau where ma_bd=?;delete from bode where ma=?");
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, made);
            pstmt.setInt(2, made);
            row = pstmt.executeUpdate();
            if (row > 0) return true;
        } catch (SQLException ex) {
            // Exception handling
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean updateDeThi(ModelDeThi dethi) {
        Connection conn = new DBConnection().getConnect();
        int row = 0;
        String query = ("UPDATE bode SET ten=?,thoi_gian_thi=?,email_tao=? where ma=?;");
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, dethi.getTende());
            pstmt.setInt(2, dethi.getThoigianthi());
            pstmt.setString(3, dethi.getEmail_tao());
            pstmt.setInt(4, dethi.getMade());
            row = pstmt.executeUpdate();
            if (row < 1) return false;
        } catch (SQLException ex) {
            // Exception handling
            ex.printStackTrace();
            return false;
        }
        query = ("delete from cau where ma_bd=?;");
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, dethi.getMade());
            row = pstmt.executeUpdate();
            if (row < 1) return false;
        } catch (SQLException ex) {
            // Exception handling
            ex.printStackTrace();
            return false;
        }
        for (ModelCauHoi x : dethi.getCauHoiArrayList()) {
            query = ("insert into cau (ma_bd,cau_hoi,phuong_an_1,phuong_an_2,phuong_an_3,phuong_an_4,dap_an) values (?,?,?,?,?,?,?);");
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(2, x.getCauhoi());
                pstmt.setString(3, x.getDapan1());
                pstmt.setString(4, x.getDapan2());
                pstmt.setString(5, x.getDapan3());
                pstmt.setString(6, x.getDapan4());
                pstmt.setString(7, x.getDapandung());
                pstmt.setInt(1, dethi.getMade());
                row = pstmt.executeUpdate();
                if (row < 1) return false;
            } catch (SQLException ex) {
                // Exception handling
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public int getSoLuongDeThi() {
        Connection conn = new DBConnection().getConnect();
        ModelUser user = new ModelUser();
        int count = 0;
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(" select * from bode;");
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    count++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public ArrayList<ModelDeThi> getAllDeThi() {
        Connection conn = new DBConnection().getConnect();
        ArrayList<ModelDeThi> arr = new ArrayList<>();
        ArrayList<ModelCauHoi> arrcauhoi = new ArrayList<>();
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(" select * from bode;");
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    ModelDeThi dethi = new ModelDeThi();
                    dethi.setEmail_tao(results.getString("email_tao"));
                    dethi.setMade(results.getInt("ma"));
                    dethi.setTende(results.getString("ten"));
                    dethi.setThoigianthi(results.getInt("thoi_gian_thi"));
                    arr.add(dethi);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    public ArrayList<ModelDeThi> getDeThiUser(String email) {
        //lấy danh sách đề thi của user
        Connection conn = new DBConnection().getConnect();
        ArrayList<ModelDeThi> arr = new ArrayList<>();
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(" select * from bode where email_tao=?;");
                pstmt.setString(1, email);
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    ModelDeThi dethi = new ModelDeThi();
                    dethi.setEmail_tao(results.getString("email_tao"));
                    dethi.setMade(results.getInt("ma"));
                    dethi.setTende(results.getString("ten"));
                    dethi.setThoigianthi(results.getInt("thoi_gian_thi"));
                    dethi.setCauHoiArrayList(getAllCauHoi(results.getInt("ma")));
                    arr.add(dethi);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    public ModelDeThi getDeThi(int made) {
        Connection conn = new DBConnection().getConnect();
        ModelDeThi deThi = new ModelDeThi();
        if (conn != null) {
            try {
                //System.out.println("oke");
                PreparedStatement pstmt = conn.prepareStatement(" select * from bode where ma=?;");
                pstmt.setInt(1, made);
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    deThi.setEmail_tao(results.getString("email_tao"));
                    deThi.setMade(results.getInt("ma"));
                    deThi.setTende(results.getString("ten"));
                    deThi.setThoigianthi(results.getInt("thoi_gian_thi"));
                    deThi.setCauHoiArrayList(getAllCauHoi(results.getInt("ma")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return deThi;
    }

    public boolean deThiChuaCoNguoiThi(int made) {
        //kiểm tra xem đề thi đã có trong table thông tin thi của db hay chưa
        Connection conn = new DBConnection().getConnect();
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(" select * from thongtinthi where ma_bd = ?;");
                pstmt.setInt(1, made);
                ResultSet results = pstmt.executeQuery();
                int count = 0;
                while (results.next()) {
                    count++;
                }
                if (count == 0) return true;
                else return false;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public int getMaxIDBoDe(String email) {
        //lấy ra giá trị max của mã đề của user, vì khi thêm đề thi mới thì giá trị mã đề tự tăng nên việc
        // kiểm soát mã đề mới được tạo ra khó khăn, nên cần lấy lại giá trị max là mã đề user mới tạo để insert câu hỏi
        Connection conn = new DBConnection().getConnect();
        int max = 0;
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ma) as max FROM bode where email_tao=?");
                pstmt.setString(1, email);
                ResultSet results = pstmt.executeQuery();

                while (results.next()) {
                    max = results.getInt("max");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return max;
    }

    //cauhoi
    public ArrayList<ModelCauHoi> getAllCauHoi(int made) {
        Connection conn = new DBConnection().getConnect();
        ArrayList<ModelCauHoi> arr = new ArrayList<>();
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(" select * from cau where ma_bd=?;");
                pstmt.setInt(1, made);
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    ModelCauHoi cauhoi = new ModelCauHoi();
                    cauhoi.setCauhoi(results.getString("cau_hoi"));
                    cauhoi.setDapandung(results.getString("dap_an"));
                    cauhoi.setDapan1(results.getString("phuong_an_1"));
                    cauhoi.setDapan2(results.getString("phuong_an_2"));
                    cauhoi.setDapan3(results.getString("phuong_an_3"));
                    cauhoi.setDapan4(results.getString("phuong_an_4"));
                    arr.add(cauhoi);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    //thi
    public boolean checkdapan(int made, String cauhoi, String cautraloi) {
        Connection conn = new DBConnection().getConnect();
        if (conn != null) {
            try {
                //System.out.println("cauhoi : "+cauhoi);
                //System.out.println("dapan : "+cautraloi);
                PreparedStatement pstmt = conn.prepareStatement(" select * from cau where ma_bd=? and cau_hoi = ?;");
                pstmt.setInt(1, made);
                pstmt.setString(2, cauhoi);
                ResultSet results = pstmt.executeQuery();
                String dapan = "";
                while (results.next()) {
                    switch (results.getString("dap_an")) {
                        case "1":
                            dapan = results.getString("phuong_an_1");
                            break;
                        case "2":
                            dapan = results.getString("phuong_an_2");
                            break;
                        case "3":
                            dapan = results.getString("phuong_an_3");
                            break;
                        case "4":
                            dapan = results.getString("phuong_an_4");
                            break;
                        default:
                            break;
                    }
                }
                //System.out.println(dapan);
                if (cautraloi.equals(dapan)) {
                    return true;
                } else return false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //thong tin thi
    public boolean ThemThongTinThi(ModelThongTinThi thongTinThi) {
        Connection conn = new DBConnection().getConnect();
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(" insert into thongtinthi (email_thi,ma_bd,diem,thoi_gian_hoan_tat) values (?,?,?,?);");
                pstmt.setString(1, thongTinThi.getEmailThi());
                pstmt.setInt(2, thongTinThi.getMade());
                pstmt.setFloat(3, thongTinThi.getDiemThi());
                pstmt.setInt(4, thongTinThi.getThoiGianHoanThanh());
                int i = pstmt.executeUpdate();
                if (i > 0) return true;
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }

    public ModelThongTinThi getThongTinThi(String email, int made) {
        Connection conn = new DBConnection().getConnect();
        ModelThongTinThi thongTinThi = new ModelThongTinThi();
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement("select * from thongtinthi where email_thi=? and ma_bd=?");
                pstmt.setString(1, email);
                pstmt.setInt(2, made);
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    thongTinThi.setDiemThi(results.getFloat("diem"));
                    thongTinThi.setEmailThi(email);
                    thongTinThi.setMade(made);
                    thongTinThi.setThoiGianHoanThanh(results.getInt("thoi_gian_hoan_tat"));
                    thongTinThi.setRank(getRank(email, made));
                }
            } catch (SQLException e) {
                return null;
            }
        }
        return thongTinThi;
    }

    public float getMaxDiemDeThi(int made) {
        Connection conn = new DBConnection().getConnect();
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(diem) as max FROM thongtinthi where ma_bd=?");
                pstmt.setInt(1, made);
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    return results.getFloat("max");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public float getMinDiemDeThi(int made) {
        Connection conn = new DBConnection().getConnect();
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement("SELECT MIN(diem) as min FROM thongtinthi where ma_bd=?");
                pstmt.setInt(1, made);
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    return results.getFloat("min");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int getRank(String email, int made) {
        Connection conn = new DBConnection().getConnect();
        ModelUser user = new ModelUser();
        int count = 0;
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement("select * from ThongTinThi where ma_bd =? order by diem desc,thoi_gian_hoan_tat asc;");
                pstmt.setInt(1, made);
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    count++;
                    if (results.getString("email_thi").trim().equals(email)) {
                        //System.out.println(count);
                        return count;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        }
        //System.out.println(count);
        return count;
    }

    public ArrayList<ModelThongTinThi> getDanhSachNguoiLamDe(int made) {
        Connection conn = new DBConnection().getConnect();
        ArrayList<ModelThongTinThi> thongTinThiArrayList = new ArrayList<>();
        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement("select email_thi,ho_ten,diem,thoi_gian_hoan_tat from ThongTinThi,users where email_thi=email and ma_bd=? order by diem desc,thoi_gian_hoan_tat asc");
                pstmt.setInt(1, made);
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    ModelThongTinThi thongTinThi = new ModelThongTinThi();
                    thongTinThi.setHoten(results.getString("ho_ten"));
                    thongTinThi.setDiemThi(results.getFloat("diem"));
                    thongTinThi.setEmailThi(results.getString("email_thi"));
                    thongTinThi.setThoiGianHoanThanh(results.getInt("thoi_gian_hoan_tat"));
                    thongTinThiArrayList.add(thongTinThi);
                }
            } catch (SQLException e) {
                return null;
            }
        }
        return thongTinThiArrayList;
    }
}
