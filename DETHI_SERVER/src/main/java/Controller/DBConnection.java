package Controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private String dbName="QLDT";
    private String host="localhost";
    private String user="sa";
    private String password="123456";
    private Connection conn = null;

    public DBConnection(){
        try {
            String dbURL = "jdbc:sqlserver://"+host+";databaseName="+dbName+";user="+user+";password="+password;
            conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                //System.out.println("DBConnected");
            }
        } catch (SQLException ex) {
            //System.err.println("Cannot connect database, " + ex);
        }
    }
    public Connection getConnect(){
        return conn;
    }
}
