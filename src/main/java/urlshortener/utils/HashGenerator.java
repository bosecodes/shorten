package urlshortener.utils;

import java.util.UUID;

public class HashGenerator {
    public static String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
