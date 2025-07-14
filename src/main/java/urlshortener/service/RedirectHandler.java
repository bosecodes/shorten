package urlshortener.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import urlshortener.database.Database;

import java.io.IOException;
import java.sql.SQLException;

public class RedirectHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String shortCode = path.substring("/s/".length());

        try {
            String longUrl = Database.getLongUrl(shortCode);
            if (longUrl == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }
            exchange.getResponseHeaders().add("Location", longUrl);
            exchange.sendResponseHeaders(302, -1);
        } catch (SQLException e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }
}
