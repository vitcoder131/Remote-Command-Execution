package remote.command.execution.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                new Thread(() -> {
                    try (
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    ) {
                        String username = in.readLine();
                        String password = in.readLine();

                        if ("admin".equals(username) && "password".equals(password)) {
                            out.println("Authentication successful.");
                            String command;
                            while ((command = in.readLine()) != null) {
                                System.out.println("Executing command: " + command);

                                try {
                                    Process process = Runtime.getRuntime().exec("cmd.exe /c " + command);
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        out.println(line);
                                    }
                                    out.println("END_OF_COMMAND");
                                } catch (Exception e) {
                                    out.println("Error executing command: " + e.getMessage());
                                    out.println("END_OF_COMMAND");
                                }
                            }
                        } else {
                            out.println("Authentication failed.");
                        }
                    } catch (Exception e) {
                        System.err.println("Error handling client: " + e.getMessage());
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (Exception e) {
                            System.err.println("Error closing client socket: " + e.getMessage());
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
