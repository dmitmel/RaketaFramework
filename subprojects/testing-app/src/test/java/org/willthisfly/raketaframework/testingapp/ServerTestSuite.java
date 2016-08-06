package org.willthisfly.raketaframework.testingapp;

import org.willthisfly.raketaframework.HTTPResponse;
import org.willthisfly.raketaframework.util.Streams;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerTestSuite {
    protected static HTTPResponse doRequest(String method, String url) {
        try {
            URL urlObject = new URL(Main.URL + url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestMethod(method);
    
            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
            byte[] body = Streams.readAllWithClosing(in);
    
            HashMap<String, String> headersInRequiredFormat = new HashMap<>(connection.getHeaderFields().size());
            for (Map.Entry<String, List<String>> headerField : connection.getHeaderFields().entrySet()) {
                headersInRequiredFormat.put(headerField.getKey(), String.join(", ", headerField.getValue()));
            }
    
            return new HTTPResponse("HTTP/1.1", connection.getResponseCode(), connection.getResponseMessage(),
                    headersInRequiredFormat, body);
        } catch (ConnectException e) {
            throw new IllegalStateException("test application isn\'t running. Please, see " +
                    "https://github.com/dmitmel/RaketaFramework/blob/master/TESTING.md");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
