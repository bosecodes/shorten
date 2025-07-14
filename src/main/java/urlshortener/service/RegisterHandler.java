package urlshortener.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.slf4j.Slf4j;
import urlshortener.utils.HelperMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class RegisterHandler implements HttpHandler {

    private static Connection conn;

    public RegisterHandler(Connection conn) {
        RegisterHandler.conn = conn;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            File file = new File("frontend/register.html");
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

        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            ps.setString(1, username);
            ps.setString(2, password);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                log.info("User registered successfully: {}", username);
                exchange.getResponseHeaders().set("Location", "/index");
                exchange.sendResponseHeaders(302, -1);
            } else {
                log.error("Failed to register user {}", username);
                HelperMethods.respond(exchange, 500, "Failed to register user");
            }
        } catch (SQLException e) {
            log.error("Error inserting user: {}", e.getMessage());
            HelperMethods.respond(exchange, 400, "User already exists");
        }
    }
}