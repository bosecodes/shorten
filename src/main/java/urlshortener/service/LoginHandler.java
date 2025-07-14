package urlshortener.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import urlshortener.utils.HelperMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginHandler implements HttpHandler {

    private static Connection conn;

    public LoginHandler(Connection conn) {
        LoginHandler.conn = conn;
    }


    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            File file = new File("frontend/login.html");
            if (!file.exists()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }
            byte[] bytes = new FileInputStream(file).readAllBytes();
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        } else if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String[] creds = HelperMethods.readRequestBody(exchange).split("&");
        String username = creds[0].split("=")[1];
        String password = creds[1].split("=")[1];

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful for user: " + username);
                String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
                exchange.getResponseHeaders().set("Location", "/index?username=" + encodedUsername);
                exchange.sendResponseHeaders(302, -1);
            } else {
                System.out.println("Invalid credentials for user: " + username);
                HelperMethods.respond(exchange, 401, "Invalid credentials");
            }
        } catch (SQLException e) {
            System.err.println("Error querying user: " + e.getMessage());
            HelperMethods.respond(exchange, 500, "Database error");
        }
    }
}
