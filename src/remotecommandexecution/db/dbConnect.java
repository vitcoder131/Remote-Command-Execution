
package remote.command.execution.db;
import java.sql.Connection;
import java.sql.DriverManager;


public class dbConnect {

    public static Connection getConnect() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=RCE;encrypt=true;trustServerCertificate=true";
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
