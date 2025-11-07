/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package remote.command.execution;

import java.sql.Connection;
import javax.swing.SwingUtilities;

/**
 *
 * @author QuocBao
 */
public class RemoteCommandExecution {

    public static void main(String[] args) {
        Connection conn = dbConnect.getConnect();
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> {
            MainForm main = new MainForm();
            main.setVisible(true);
            main.setLocationRelativeTo(null);
        });
    
}
}
