package ru.netology.server.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Leonid Zulin
 * @date 11.03.2023 15:57
 */
public class Request {
    // request method
    private final String method;
    // request path
    private final String path;
    // storing headers in the map
    private final Map<String, String> headers;
    // request body
    private final InputStream body;

    // constructor with our parameters, it's private, so that no one can call it
    private Request(String method, String path, Map<String, String> headers, InputStream body) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", headers=" + headers +
                '}';
    }

    // getters
    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public InputStream getBody() {
        return body;
    }

    // this method to parse the request
    public static Request fromInputStream(InputStream inputStream) throws IOException {
        final var in = new BufferedReader(new InputStreamReader(inputStream));
        // read only request line for simplicity
        // must be in form GET /path HTTP/1.1
        final var requestLine = in.readLine();
        final var parts = requestLine.split(" ");

        if (parts.length != 3) {
            // throw exception
            throw new IOException("Invalid request!!!");
        }

        // if the request is valid
        String method = parts[0];
        String path = parts[1];

        // Map for headers
        Map<String, String> headers = new HashMap<>();
        String intermediateLine;

        // read to a variable from our InputStream
        // and immediately check that the next value is not Empty
        // because the separator between header and body is an empty string before
        while (!(intermediateLine = in.readLine()).isEmpty()) {
            // parse the value of each header by removing the colon
            int index = intermediateLine.indexOf(":");
            String nameHeader = intermediateLine.substring(0, index); // name before colon
            String valueHeader = intermediateLine.substring(index + 2); // value after two symbol includes colon and space

            // add our Map
            headers.put(nameHeader, valueHeader);
        }
        // creating an object class Request
        Request request = new Request(method, path, headers, inputStream);

        // returning the object from class Request
        return request;
    }


}
