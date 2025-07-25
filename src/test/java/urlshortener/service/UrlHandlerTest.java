package urlshortener.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import urlshortener.database.Database;

import static org.junit.jupiter.api.Assertions.*;

public class UrlHandlerTest {

    @BeforeAll
    static void setupDatabase() {
        // Use in-memory DB for isolated testing
        Database.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        Database.initSchema(); // Should now create the `username` column too
    }

    @Test
    void testInsertAndRetrieveUrlWithUsername() throws Exception {
        String username = "testuser";
        String shortCode = "abc123";
        String longUrl = "https://example.com";

        Database.insertUrl(username, shortCode, longUrl);
        String fetched = Database.getLongUrl(username, shortCode);

        assertEquals(longUrl, fetched);
    }

}
