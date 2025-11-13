package remotecommandexecution;

import remotecommandexecution.db.dbConnect;
import java.sql.Connection;
import javax.swing.SwingUtilities;
import remotecommandexecution.gui.ClientGui;
import remotecommandexecution.gui.LoginForm;

public class RemoteCommandExecution {

    public static void main(String[] args) {
        Connection conn = dbConnect.getConnect();
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> {
            LoginForm login = new LoginForm();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
        });
    
}
}
