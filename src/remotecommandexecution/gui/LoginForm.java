package remotecommandexecution.gui;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;
import java.io.*;
import java.security.MessageDigest;

public class LoginForm extends JFrame {

    JTextField txtServer = new JTextField("localhost", 15);
    JTextField txtPort = new JTextField("12345", 15);
    JTextField txtUsername = new JTextField(15);
    JPasswordField txtPassword = new JPasswordField(15);

    public LoginForm() {
        setTitle("Đăng nhập Remote Command Execution");
        setSize(400, 300);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        main.add(field("Server:", txtServer));
        main.add(field("Port:", txtPort));
        main.add(field("Username:", txtUsername));
        main.add(field("Password:", txtPassword));

        JButton login = new JButton("Kết nối và đăng nhập");
        login.setAlignmentX(Component.CENTER_ALIGNMENT);
        login.addActionListener(e -> login());
        main.add(Box.createVerticalStrut(10));
        main.add(login);

        add(main);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel field(String label, JComponent input) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel(label));
        p.add(input);
        return p;
    }

    private String hashSHA256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes("UTF-8"));

            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) hex.append(String.format("%02X", b));

            return hex.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private void login() {
        try {
            Socket socket = new Socket(txtServer.getText(),
                    Integer.parseInt(txtPort.getText()));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out.println(txtUsername.getText());
            out.println(hashSHA256(new String(txtPassword.getPassword())));

            String resp = in.readLine();

            if (resp.startsWith("AUTH_OK")) {
                String fullName = resp.split(":")[1];
                new ClientGui(socket, in, out, fullName);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Sai username hoặc mật khẩu!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
