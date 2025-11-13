package remotecommandexecution.db;

import remotecommandexecution.model.CommandHistory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandHistoryDAO {

    public void saveHistory(int userId, String serverIp, String command, String result, String clientIp) {

        String sql = """
            INSERT INTO CommandHistory(user_id, server_ip, command, result, client_ip)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = dbConnect.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, serverIp);
            ps.setString(3, command);
            ps.setString(4, result);
            ps.setString(5, clientIp);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CommandHistory> getAll() {
        List<CommandHistory> list = new ArrayList<>();

        String sql = """
            SELECT h.history_id, u.username, h.server_ip, h.command,
                   h.result, h.error_message, h.client_ip
            FROM CommandHistory h
            JOIN Users u ON u.user_id = h.user_id
            ORDER BY h.history_id DESC
        """;

        try (Connection conn = dbConnect.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new CommandHistory(
                    rs.getInt("history_id"),
                    rs.getString("username"),
                    rs.getString("server_ip"),
                    rs.getString("command"),
                    rs.getString("result"),
                    rs.getString("error_message"),
                    rs.getString("client_ip")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
