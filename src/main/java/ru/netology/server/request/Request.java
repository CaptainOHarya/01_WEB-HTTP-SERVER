package ru.netology.server.request;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private static List<NameValuePair> pathQuery = new ArrayList<>();
    private static Map<String, String> resultQuery = new HashMap<>();
    // storing headers in the map
    private final Map<String, String> headers;
    // request body
    private final InputStream body;
    // header delimiter
    private final static String HEADER_DELIMITER = ":";

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
        String path;

        if (parts[1].contains("?")) {
            path = parts[1].substring(0, parts[1].indexOf("?"));
            pathQuery = getQueryParam(parts[1].substring(parts[1].indexOf("?") + 1));
            resultQuery = getQueryParams();
            System.out.println("Полученные Query параметры: ");
            for (Map.Entry<String, String> entry : resultQuery.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }
        } else path = parts[1];

        // Map for headers
        Map<String, String> headers = new HashMap<>();
        String intermediateLine;

        // read to a variable from our InputStream
        // and immediately check that the next value is not Empty
        // because the separator between header and body is an empty string before
        while (!(intermediateLine = in.readLine()).isEmpty()) {
            // parse the value of each header by removing the colon
            int index = intermediateLine.indexOf(HEADER_DELIMITER);
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

    public static List<NameValuePair> getQueryParam(String pathQuery) {
        List<NameValuePair> extraList = URLEncodedUtils.parse(pathQuery, Charset.forName("UTF-8"));
        return extraList;
    }

    public static Map<String, String> getQueryParams() {
        for (NameValuePair item : pathQuery) {
            String name = item.getName();
            String value;
            int i = 0;
            while (resultQuery.containsKey(name)) {
                name = item.getName() + ++i;
            }
            value = item.getValue();
            resultQuery.put(name, value);

        }
        return resultQuery;

    }


}



