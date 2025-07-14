package urlshortener.config;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    public static int PORT;
    public static String BASE_URL;
    public static String DB_URL;
    public static String DB_USER;
    public static String DB_PASS;

    static {
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(in);

            PORT = Integer.parseInt(props.getProperty("server.port"));
            BASE_URL = props.getProperty("app.baseUrl");
            DB_URL = props.getProperty("database.url");
            DB_USER = props.getProperty("database.username");
            DB_PASS = props.getProperty("database.password");

        } catch (Exception e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }
}
