package bg.softuni.errors.web;

import java.util.Map;

// Limited, client-friendly error body: no stack traces or internals leak to the client.
public record ErrorInfo(String url, String message, Map<String, String> fieldErrors) {

    public ErrorInfo(String url, String message) {
        this(url, message, Map.of());
    }
}
