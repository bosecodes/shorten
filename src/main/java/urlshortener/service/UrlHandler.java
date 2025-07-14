package urlshortener.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import urlshortener.config.AppConfig;
import urlshortener.database.Database;
import urlshortener.utils.HashGenerator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class UrlHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining());

        String longUrl = body.replace("url=", "").trim();
        if (longUrl.isEmpty()) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }

        String shortCode = HashGenerator.generateShortCode();
        try {
            Database.insertUrl(shortCode, longUrl);
        } catch (SQLException e) {
            exchange.sendResponseHeaders(500, -1);
            return;
        }

        String shortUrl = AppConfig.BASE_URL + shortCode;
        byte[] response = shortUrl.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
