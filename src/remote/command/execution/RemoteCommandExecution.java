/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package remote.command.execution;

import remote.command.execution.db.dbConnect;
import java.sql.Connection;
import javax.swing.SwingUtilities;
import remote.command.execution.gui.ClientGui;

/**
 *
 * @author QuocBao
 */
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
