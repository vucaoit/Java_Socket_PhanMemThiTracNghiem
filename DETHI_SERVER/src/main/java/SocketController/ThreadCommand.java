package SocketController;

import Controller.ControllerDB;
import Model.Manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ThreadCommand extends Thread {
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private ControllerDB db = new ControllerDB();

    public void run() {
        String command = "";
        String help = "/online : hiển thị số người đang online\n" +
                "/soluonguser : hiển thị số lượng user trong hệ thống\n" +
                "/lock email x : lock chức năng user. Trong đó nếu email user muốn lock, x là 0 thì không lock, 1 lock login, 2 lock thi, 3 lock tạo đề, 23 lock thi và tạo đề\n" +
                "/soluongdethi : Hiện thị số lượng đề thi trong hệ thống\n" +
                "/diemcao x : hiển thị điểm cao nhất của đề thi, trong đó x là mã đề\n" +
                "/diemthap x : hiển thị điểm thấp nhất của đề thi, trong đó x là mã đề\n";
        while (true) {
            try {
                command = input.readLine();//lấy input(cú pháp) từ bàn phím

                switch (command.split(" ")[0]) {
                    case "/online":
                        if (command.split(" ").length == 1) {
                            System.out.println("Số user đang online : " + Manager.userOnline.size());
                        } else System.out.println("cú pháp không đúng. Gõ /help để hiện thị cú pháp");
                        break;
                    case "/soluonguser":
                        if (command.split(" ").length == 1) {
                            System.out.println("Số lượng user trong hệ thống : " + db.getSoluongUser());
                        } else System.out.println("cú pháp không đúng. Gõ /help để hiện thị cú pháp");
                        break;
                    case "/lock":
                        if (command.split(" ").length == 3) {
                            try {
                                int option = Integer.parseInt(command.split(" ")[2]);
                                if (option == 0 || option == 1 || option == 2 || option == 3 || option == 23) {
                                    if (db.LockUser(command.split(" ")[1], option))
                                        System.out.println("Đã lock user " + command.split(" ")[1]);
                                    else System.out.println("Không có user sử dụng email " + command.split(" ")[1]);
                                } else {
                                    System.out.println("cú pháp không đúng. Gõ /help để hiện thị cú pháp");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("cú pháp không đúng. Gõ /help để hiện thị cú pháp");
                            }
                        } else System.out.println("cú pháp không đúng. Gõ /help để hiện thị cú pháp");
                        break;
                    case "/soluongdethi":
                        if (command.split(" ").length == 1) {
                            System.out.println("Số lượng đề thi trong hệ thống : " + db.getSoLuongDeThi());
                        } else System.out.println("cú pháp không đúng. Gõ /help để hiện thị cú pháp");
                        break;
                    case "/diemcao":
                        if (command.split(" ").length == 2) {
                            try {
                                int made = Integer.parseInt(command.split(" ")[1]);
                                float diem = db.getMaxDiemDeThi(made);
                                if (diem != -1) System.out.println("Điểm cao nhất : " + diem);
                                else System.out.println("Không có đề thi " + made + " hoặc đề thi chưa ai thi");
                            } catch (NumberFormatException e) {
                                System.out.println("cú pháp không đúng. Gõ /help để hiện thị cú pháp");
                            }
                        } else System.out.println("cú pháp không đúng. Gõ /help để hiện thị cú pháp");
                        break;
                    case "/diemthap":
                        if (command.split(" ").length == 2) {
                            try {
                                int made = Integer.parseInt(command.split(" ")[1]);
                                float diem = db.getMinDiemDeThi(made);
                                if (diem != -1) System.out.println("Điểm thấp nhất : " + diem);
                                else System.out.println("Không có đề thi " + made + " hoặc đề thi chưa ai thi");
                            } catch (NumberFormatException e) {
                                System.out.println("cú pháp không đúng. Gõ /help để hiện thị cú pháp");
                            }
                        } else System.out.println("cú pháp không đúng. Gõ /help để hiện thị cú pháp");
                        break;
                    case "/help":
                        if (command.split(" ").length == 1) {
                            System.out.println(help);
                        } else System.out.println("cú pháp không đúng. Gõ /help để hiện thị cú pháp");
                        break;
                    default:
                        System.out.println("cú pháp không đúng. Gõ /help để hiện thị cú pháp");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
