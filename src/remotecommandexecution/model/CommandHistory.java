package remotecommandexecution.model;

public class CommandHistory {
    private int id;
    private String username;
    private String serverIp;
    private String command;
    private String result;
    private String error;
    private String clientIp;

    public CommandHistory(int id, String username, String serverIp,
                          String command, String result, String error, String clientIp) {
        this.id = id;
        this.username = username;
        this.serverIp = serverIp;
        this.command = command;
        this.result = result;
        this.error = error;
        this.clientIp = clientIp;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getServerIp() { return serverIp; }
    public String getCommand() { return command; }
    public String getResult() { return result; }
    public String getError() { return error; }
    public String getClientIp() { return clientIp; }
}
