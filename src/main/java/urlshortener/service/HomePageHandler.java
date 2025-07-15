package urlshortener.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

public class HomePageHandler implements HttpHandler {
    private static Connection conn;
    String fileLocation = "frontend/index.html";

    public HomePageHandler(Connection conn) {
        HomePageHandler.conn = conn;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String username = "Guest";
        if (query != null && query.startsWith("username=")) {
            String encodedUsername = query.split("=")[1];
            username = URLDecoder.decode(encodedUsername, StandardCharsets.UTF_8.toString());
        }

        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }
        String html = new String(Files.readAllBytes(Paths.get(fileLocation)));
        html = html.replace("{{username}}", username);

        byte[] response = html.getBytes();
        exchange.getResponseHeaders().add("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
}