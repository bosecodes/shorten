package urlshortener.database;

import lombok.Setter;
import urlshortener.config.AppConfig;

import java.sql.*;

public class Database {
    @Setter
    private static String jdbcUrl = AppConfig.DB_URL; // default

    public static void initSchema() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE if not exists urls (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "username VARCHAR(255),"+
                    "short_code VARCHAR(50) UNIQUE, " +
                    "long_url TEXT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, "sa", "");
    }

    public static void insertUrl(String username, String shortCode, String longUrl) throws SQLException {
        String sql = "INSERT INTO urls (username, short_code, long_url) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, shortCode);
            stmt.setString(3, longUrl);
            stmt.executeUpdate();
        }
    }

    public static String getLongUrl(String username, String shortCode) throws SQLException {
        String sql = "SELECT long_url FROM urls WHERE username = ? AND short_code = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, shortCode);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("long_url") : null;
        }
    }
}
