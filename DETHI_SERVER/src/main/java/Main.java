import SocketController.ThreadCommand;
import SocketController.ThreadSocket;
//4
import java.io.*;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) {
        int port = 4949;//port mà server chạy phải khớp với client
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("server starting...");
            new ThreadCommand().start();//chạy luồng nhập cú pháp
            while (true) {
                // Su dung multithread
                // khi có 1 client kết nối tới thì server tạo ra 1 luồng mới cho việc kết nối đến client
                new ThreadSocket(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.out.println("port da duoc su dung, chon port khac hoac tat port " + port + " o tien trinh khac");
        }
    }
}
