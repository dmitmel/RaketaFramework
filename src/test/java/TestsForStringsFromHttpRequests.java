import github.dmitmel.raketaframework.web.HTTPRequest;
import github.dmitmel.raketaframework.web.URL;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class TestsForStringsFromHttpRequests extends Assert {
    @Test
    public void testNothing() {
        HTTPRequest message = new HTTPRequest("GET", new URL("/"), "HTTP/1.1");

        String expected = "GET / HTTP/1.1\r\n" +
                "\r\n";
        assertEquals(expected, message.toString());
    }

    @Test
    public void testHeadersAndBody() {
        HashMap<String, String> headers = new HashMap<>(2);
        headers.put("A", "B");
        headers.put("C", "D");
        HTTPRequest message = new HTTPRequest("GET", new URL("/"), "HTTP/1.1", headers, "Some text.");

        String expected = "GET / HTTP/1.1\r\n" +
                "A: B\r\n" +
                "C: D\r\n" +
                "\r\n" +
                "Some text.";
        assertEquals(expected, message.toString());
    }

    @Test
    public void testHeaders() {
        HashMap<String, String> headers = new HashMap<>(2);
        headers.put("A", "B");
        headers.put("C", "D");
        HTTPRequest message = new HTTPRequest("GET", new URL("/"), "HTTP/1.1", headers, "");

        String expected = "GET / HTTP/1.1\r\n" +
                "A: B\r\n" +
                "C: D\r\n" +
                "\r\n";
        assertEquals(expected, message.toString());
    }

    @Test
    public void testBody() {
        HTTPRequest message = new HTTPRequest("GET", new URL("/"), "HTTP/1.1", "Some text.");

        String expected = "GET / HTTP/1.1\r\n" +
                "\r\n" +
                "Some text.";
        assertEquals(expected, message.toString());
    }
}
