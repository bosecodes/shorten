package urlshortener.service;


import org.junit.jupiter.api.*;
import urlshortener.FakeHttpExchange;
import urlshortener.config.AppConfig;
import urlshortener.service.HomePageHandler;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.nio.charset.StandardCharsets.UTF_8;

class HomePageHandlerTest {

    Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");

    private static final String HTML_TEMPLATE =
            "<html><body><h1>Welcome, {{username}}</h1></body></html>";
    private static final String INDEX_PATH = "frontendtest/index.html";

    HomePageHandlerTest() throws SQLException {
    }

    @BeforeAll
    static void setup() throws Exception {
        Files.createDirectories(Paths.get("frontendtest"));
        try (PrintWriter writer = new PrintWriter(new FileWriter(INDEX_PATH))) {
            writer.write(HTML_TEMPLATE);
        }
    }

//    @AfterAll
//    static void cleanup() throws Exception {
//        Files.deleteIfExists(Paths.get(INDEX_PATH));
//        Files.deleteIfExists(Paths.get("frontendtest"));
//    }

    @Test
    void testHomePageHandlerWithUsernameQuery() throws Exception {
        FakeHttpExchange exchange = new FakeHttpExchange("GET", "/?username=sa");
        HomePageHandler handler = new HomePageHandler(conn);
        handler.fileLocation = INDEX_PATH;

        handler.handle(exchange);

        // Properly read the actual HTML string from the output stream
        String response = convertStreamToString((ByteArrayOutputStream) exchange.getResponseBody()); // custom method that does .toString(StandardCharsets.UTF_8)
        Assertions.assertTrue(response.contains("Welcome, sa"));
    }

    @Test
    void testHomePageHandlerWithoutQuery() throws Exception {
        FakeHttpExchange exchange = new FakeHttpExchange("GET", "/");
        HomePageHandler handler = new HomePageHandler(conn);
        handler.fileLocation = INDEX_PATH;

        handler.handle(exchange);

        String response = convertStreamToString((ByteArrayOutputStream) exchange.getResponseBody()); // custom method that does .toString(StandardCharsets.UTF_8)
        Assertions.assertFalse(response.contains("Welcome, sa"));
    }

    public String convertStreamToString(ByteArrayOutputStream outputStream) {
        if (outputStream == null) {
            return "";
        }
        return outputStream.toString(StandardCharsets.UTF_8);
    }
}
