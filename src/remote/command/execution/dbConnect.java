/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package remote.command.execution;
import java.sql.Connection;

import java.sql.DriverManager;

/**
 *
 * @author QuocBao
 */
public class dbConnect {

    public static Connection getConnect() {
        String url = "jdbc:sqlserver://DESKTOP-QP0T1SS:1433;databaseName=QuanLiOto;encrypt=true;trustServerCertificate=true";
        String user = "sa";
        String password = "12";
        Connection conn = null;
        try {
            conn  = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối thành công");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Kết nối thành công");
        }
        return conn;
    }

    public void closeConnect(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                
            } catch (Exception e) {
             
                e.printStackTrace();
            }
        }
    }
}
