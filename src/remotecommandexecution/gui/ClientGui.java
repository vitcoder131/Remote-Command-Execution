package remotecommandexecution.gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientGui extends JFrame {

    private PrintWriter out;
    private BufferedReader in;

    private JTextArea outputArea = new JTextArea();
    private JTextField commandField = new JTextField();

    public ClientGui(Socket socket, BufferedReader in, PrintWriter out, String fullName) {
        this.in = in;
        this.out = out;

        setTitle("Remote Command - User: " + fullName);
        setSize(900, 550);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        JButton history = new JButton("Xem lịch sử");
        top.add(history);
        history.addActionListener(e -> new HistoryForm());
        add(top, BorderLayout.NORTH);

        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        JButton send = new JButton("Gửi");
        bottom.add(commandField, BorderLayout.CENTER);
        bottom.add(send, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        send.addActionListener(e -> sendCommand());
        commandField.addActionListener(e -> sendCommand());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void sendCommand() {
        String cmd = commandField.getText();
        if (cmd.isEmpty()) return;

        out.println(cmd);
        commandField.setText("");

        new Thread(() -> {
            try {
                String line;
                while (!(line = in.readLine()).equals("END")) {
                    outputArea.append(line + "\n");
                }
                outputArea.append("------ END ------\n\n");
            } catch (Exception e) {
                outputArea.append("Lỗi đọc dữ liệu: " + e.getMessage());
            }
        }).start();
    }
}
