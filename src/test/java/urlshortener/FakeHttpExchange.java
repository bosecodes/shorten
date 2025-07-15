package urlshortener;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import lombok.Getter;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FakeHttpExchange extends HttpExchange {
    private final ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
    private final Headers requestHeaders = new Headers();
    private final Headers responseHeaders = new Headers();
    private final URI uri;
    private final String method;
    private ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
    private InputStream requestBody = InputStream.nullInputStream();
    @Getter
    private int statusCode;


    public FakeHttpExchange(String method, String uri) {
        this.method = method;
        this.uri = URI.create(uri);
    }

    @Override public Headers getRequestHeaders() { return requestHeaders; }
    @Override public Headers getResponseHeaders() { return responseHeaders; }
    @Override public URI getRequestURI() { return uri; }
    @Override public String getRequestMethod() { return method; }
    @Override public OutputStream getResponseBody() { return responseStream; }
    @Override public InputStream getRequestBody() { return InputStream.nullInputStream(); }

    @Override public HttpContext getHttpContext() { return null; }
    @Override public void close() {}
    @Override public InetSocketAddress getRemoteAddress() { return null; }

    public String getResponseText() {
        return responseBody.toString(StandardCharsets.UTF_8);
    }

    @Override
    public int getResponseCode() {
        return 0;
    }

    @Override
    public void sendResponseHeaders(int code, long length) {
        this.statusCode = code;
    }

    @Override public InetSocketAddress getLocalAddress() { return null; }
    @Override public String getProtocol() { return null; }
    @Override public Object getAttribute(String s) { return null; }
    @Override public void setAttribute(String s, Object o) {}
    @Override public void setStreams(InputStream inputStream, OutputStream outputStream) {}

    @Override
    public HttpPrincipal getPrincipal() {
        return null;
    }

    public void setRequestBody(String body) {
        this.requestBody = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
    }
}
