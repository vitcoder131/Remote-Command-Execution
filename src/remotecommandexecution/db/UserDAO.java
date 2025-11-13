package remotecommandexecution.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import remotecommandexecution.model.User;

public class UserDAO {

    public User login(String username, String passwordHash) {
        String sql = "SELECT * FROM Users WHERE username=? AND password_hash=?";

        try (Connection conn = dbConnect.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, passwordHash);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("full_name"),
                    rs.getBoolean("is_admin")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
