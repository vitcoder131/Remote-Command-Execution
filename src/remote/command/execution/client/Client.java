package remote.command.execution.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in))
        {
            System.out.println("Connected to the server. Enter your credentials.");

            System.out.print("Username: ");
            String username = scanner.nextLine();
            out.println(username);

            System.out.print("Password: ");
            String password = scanner.nextLine();
            out.println(password);

            String authResponse = in.readLine();
            System.out.println(authResponse);

            if ("Authentication successful.".equals(authResponse)) {
                System.out.println("Enter commands to execute.");
                while (true) {
                    System.out.print("> ");
                    String command = scanner.nextLine();

                    if ("exit".equalsIgnoreCase(command)) {
                        break;
                    }

                    out.println(command);

                    String line;
                    while (!(line = in.readLine()).equals("END_OF_COMMAND")) {
                        System.out.println(line);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
