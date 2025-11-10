package remote.command.execution.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientGui extends JFrame {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private JTextField serverAddressField = new JTextField("localhost", 15);
    private JTextField portField = new JTextField("12345", 5);
    private JTextField usernameField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
    private JButton connectButton = new JButton("Connect");
    private JTextArea outputArea = new JTextArea(20, 50);
    private JTextField commandField = new JTextField(40);
    private JButton sendButton = new JButton("Send");

    public ClientGui() {
        super("RCE Client");

        JPanel connectionPanel = new JPanel(new FlowLayout());
        connectionPanel.add(new JLabel("Server Address:"));
        connectionPanel.add(serverAddressField);
        connectionPanel.add(new JLabel("Port:"));
        connectionPanel.add(portField);
        connectionPanel.add(new JLabel("Username:"));
        connectionPanel.add(usernameField);
        connectionPanel.add(new JLabel("Password:"));
        connectionPanel.add(passwordField);
        connectionPanel.add(connectButton);

        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel commandPanel = new JPanel(new FlowLayout());
        commandPanel.add(new JLabel("Command:"));
        commandPanel.add(commandField);
        commandPanel.add(sendButton);

        setLayout(new BorderLayout());
        add(connectionPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(commandPanel, BorderLayout.SOUTH);

        connectButton.addActionListener(e -> connect());
        sendButton.addActionListener(e -> sendCommand());
        commandField.addActionListener(e -> sendCommand());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void connect() {
        try {
            String serverAddress = serverAddressField.getText();
            int port = Integer.parseInt(portField.getText());
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println(usernameField.getText());
            out.println(new String(passwordField.getPassword()));

            String response = in.readLine();
            outputArea.append(response + "\n");

            if (!"Authentication successful.".equals(response)) {
                socket.close();
            }
        } catch (Exception e) {
            outputArea.append("Connection error: " + e.getMessage() + "\n");
        }
    }

    private void sendCommand() {
        if (out != null) {
            String command = commandField.getText();
            out.println(command);
            commandField.setText("");

            new Thread(() -> {
                try {
                    String line;
                    while (!(line = in.readLine()).equals("END_OF_COMMAND")) {
                        final String finalLine = line;
                        SwingUtilities.invokeLater(() -> outputArea.append(finalLine + "\n"));
                    }
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> outputArea.append("Error receiving data: " + e.getMessage() + "\n"));
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientGui::new);
    }
}
