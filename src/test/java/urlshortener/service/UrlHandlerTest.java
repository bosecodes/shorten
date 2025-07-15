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
        Database.initSchema(); // Create `urls` table
    }

    @Test
    void testInsertAndRetrieveUrl() throws Exception {
        String shortCode = "abc123";
        String longUrl = "https://example.com";

        Database.insertUrl(shortCode, longUrl);
        String fetched = Database.getLongUrl(shortCode);

        assertEquals(longUrl, fetched);
    }

}
