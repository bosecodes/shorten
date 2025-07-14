package urlshortener.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HashGeneratorTest {

    @Test
    void testGenerateShortCode() {
        // Generate a short code
        String shortCode = HashGenerator.generateShortCode();

        // Assert that the short code is not null
        assertNotNull(shortCode, "Short code should not be null");

        // Assert that the short code is not empty
        assertFalse(shortCode.isEmpty(), "Short code should not be empty");

        // Assert that the short code has the expected length
        assertEquals(6, shortCode.length(), "Short code should be 6 characters long");
    }
}