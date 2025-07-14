package urlshortener.config;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    @Test
    void testAppConfigPropertiesLoading() {
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            assertNotNull(in, "application.properties file should exist");

            Properties props = new Properties();
            props.load(in);

            // Verify individual properties
            assertEquals("8080", props.getProperty("server.port"), "server.port should match");
            assertEquals("http://localhost:8080", props.getProperty("app.baseUrl"), "app.baseUrl should match");
            assertEquals("jdbc:h2:./shorten-db", props.getProperty("database.url"), "database.url should match");
            assertEquals("sa", props.getProperty("database.username"), "database.username should match");
            assertEquals("", props.getProperty("database.password"), "database.password should match");
        } catch (Exception e) {
            fail("Failed to load application.properties: " + e.getMessage());
        }
    }
}