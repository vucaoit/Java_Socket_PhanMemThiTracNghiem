package Model;

import java.io.Serializable;

public class DataTransport  implements Serializable {//implements Serializable thì mới gửi được qua socket ở dạng object
    private String f = "";
    private String d = "";
//thằng này dùng để gửi dữ liệu qua lại giữa client và server
    //DataStransport của client và của server phải giống y hệt nhau
    public DataTransport() {
    }
    public DataTransport(String function, String data) {
        this.f = function;
        this.d = data;
    }

    public String getFunction() {
        return f;
    }

    public void setFunction(String function) {
        this.f = function;
    }

    public String getData() {
        return d;
    }

    public void setData(String data) {
        this.d = data;
    }

}
