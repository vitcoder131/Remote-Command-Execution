package remotecommandexecution.gui;

import remotecommandexecution.db.CommandHistoryDAO;
import remotecommandexecution.model.CommandHistory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoryForm extends JFrame {

    DefaultTableModel model = new DefaultTableModel(
        new String[]{"ID", "User", "Server IP", "Command", "Result", "Error", "Client IP"}, 0
    );

    JTable table = new JTable(model);

    public HistoryForm() {
        setTitle("Lịch sử lệnh");
        setSize(900, 500);
        setLayout(new BorderLayout());

        table.setRowHeight(28);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton reload = new JButton("Tải lại");
        reload.addActionListener(e -> load());
        add(reload, BorderLayout.NORTH);

        load();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void load() {
        model.setRowCount(0);
        CommandHistoryDAO dao = new CommandHistoryDAO();

        for (CommandHistory h : dao.getAll()) {
            model.addRow(new Object[]{
                h.getId(), h.getUsername(), h.getServerIp(),
                h.getCommand(), h.getResult(),
                h.getError(), h.getClientIp()
            });
        }
    }
}
