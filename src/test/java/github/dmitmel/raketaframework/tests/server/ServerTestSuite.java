package github.dmitmel.raketaframework.tests.server;

import github.dmitmel.raketaframework.tests.TestApplication;
import github.dmitmel.raketaframework.util.Streams;
import github.dmitmel.raketaframework.HTTPResponse;
import org.junit.Assert;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerTestSuite extends Assert {
    protected static HTTPResponse doRequest(String method, String url) {
        try {
            URL urlObject = new URL(TestApplication.URL + url);
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
