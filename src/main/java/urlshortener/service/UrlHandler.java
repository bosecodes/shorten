package urlshortener.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import urlshortener.config.AppConfig;
import urlshortener.database.Database;
import urlshortener.utils.HashGenerator;
import urlshortener.utils.HelperMethods;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class UrlHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining());

        Map<String, String> formMap = HelperMethods.parseFormData(requestBody);

        String longUrl = formMap.get("longUrl");
        String username = formMap.get("username");
        boolean isGuest = (username == null || username.isEmpty());

        String prefix = isGuest ? "" : (username.length() > 8 ? username.substring(0, 8) : username);
        String shortCode = (isGuest ? "" : prefix + "_") + HashGenerator.generateShortCode();


        if (longUrl == null || longUrl.isEmpty()) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }


        try {
            Database.initSchema();
            Database.insertUrl(username, longUrl, shortCode);
        } catch (SQLException e) {
            exchange.sendResponseHeaders(500, -1);
            return;
        }

        String shortUrl = AppConfig.BASE_URL + shortCode;
        byte[] response = shortUrl.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = exchange.getResponseBody(); os) {
            os.write(response);
        }
    }
}
