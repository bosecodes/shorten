package urlshortener.database;

import java.sql.*;

public class Database {
    private static final String JDBC_URL = "jdbc:h2:./shorten-db";

    static {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS urls (" +
                    "id IDENTITY PRIMARY KEY, " +
                    "short_code VARCHAR(20) UNIQUE, " +
                    "long_url TEXT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, "sa", "");
    }

    public static void insertUrl(String shortCode, String longUrl) throws SQLException {
        String sql = "INSERT INTO urls (short_code, long_url) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, shortCode);
            stmt.setString(2, longUrl);
            stmt.executeUpdate();
        }
    }

    public static String getLongUrl(String shortCode) throws SQLException {
        String sql = "SELECT long_url FROM urls WHERE short_code = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, shortCode);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("long_url") : null;
        }
    }
}
