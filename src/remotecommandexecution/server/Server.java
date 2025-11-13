package remotecommandexecution.server;

import remotecommandexecution.db.UserDAO;
import remotecommandexecution.db.CommandHistoryDAO;
import remotecommandexecution.model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {

            System.out.println(">> SERVER RUNNING ON PORT 12345");

            while (true) {
                Socket client = serverSocket.accept();
                new Thread(() -> handleClient(client)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String username = in.readLine();
            String passwordHash = in.readLine();

            UserDAO userDAO = new UserDAO();
            User user = userDAO.login(username, passwordHash);

            if (user == null) {
                out.println("AUTH_FAILED");
                socket.close();
                return;
            }

            out.println("AUTH_OK:" + user.getFullName());

            CommandHistoryDAO historyDAO = new CommandHistoryDAO();

            String command;
            while ((command = in.readLine()) != null) {

                Process p = Runtime.getRuntime().exec("cmd.exe /c " + command);
                BufferedReader cmdReader = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = cmdReader.readLine()) != null) {
                    out.println(line);
                    result.append(line).append("\n");
                }
                out.println("END");

                // Lưu lịch sử vào DB
                historyDAO.saveHistory(
                        user.getId(),
                        socket.getInetAddress().toString(),
                        command,
                        result.toString(),
                        socket.getInetAddress().toString()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
