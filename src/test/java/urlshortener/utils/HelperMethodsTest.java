package urlshortener.utils;

import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HelperMethodsTest {

    @Test
    void testReadRequestBody() throws IOException {
        // Mock HttpExchange
        HttpExchange exchange = mock(HttpExchange.class);

        // Mock InputStream with sample request body
        String requestBody = "testRequestBody";
        InputStream inputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
        when(exchange.getRequestBody()).thenReturn(inputStream);

        // Call the method
        String result = HelperMethods.readRequestBody(exchange);

        // Verify the result
        assertEquals(requestBody, result, "The request body should match the expected value");
    }

    @Test
    void testRespond() throws IOException {
        // Mock HttpExchange
        HttpExchange exchange = mock(HttpExchange.class);

        // Mock OutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(outputStream);

        // Call the method
        String response = "testResponse";
        HelperMethods.respond(exchange, 200, response);

        // Verify the response headers
        verify(exchange).sendResponseHeaders(200, response.getBytes().length);

        // Verify the response body
        assertEquals(response, outputStream.toString(), "The response body should match the expected value");
    }
}