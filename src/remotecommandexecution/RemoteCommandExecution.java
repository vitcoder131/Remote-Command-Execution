package remote.command.execution;

import remote.command.execution.db.dbConnect;
import java.sql.Connection;
import javax.swing.SwingUtilities;
import remote.command.execution.gui.ClientGui;

public class RemoteCommandExecution {

    public static void main(String[] args) {
        Connection conn = dbConnect.getConnect();
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> {
            ClientGui clientgui = new ClientGui();
            clientgui.setVisible(true);
            clientgui.setLocationRelativeTo(null);
        });
    
}
}
