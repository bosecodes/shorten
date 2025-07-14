package urlshortener;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.sql.*;

import lombok.extern.slf4j.Slf4j;
import urlshortener.config.AppConfig;
import urlshortener.service.*;

@Slf4j
public class Server {

    private static Connection conn;

    public static void main(String[] args) throws Exception {
        // Setup H2 database in memory
        conn = DriverManager.getConnection(AppConfig.DB_URL, AppConfig.DB_USER, AppConfig.DB_PASS);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE users (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255))");
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(AppConfig.PORT), 0);
        server.createContext("/register", new RegisterHandler(getConn()));
        server.createContext("/login", new LoginHandler(getConn()));
        server.createContext("/index", new HomePageHandler(getConn()));
        server.createContext("/", new HomePageHandler(getConn()));
        server.createContext("/shorten", new UrlHandler());
        server.createContext("/s", new RedirectHandler());
        server.setExecutor(null);
        server.start();

        log.info("Server running on http://localhost:8080/");
    }

    public static Connection getConn() {
        return conn;
    }
}
