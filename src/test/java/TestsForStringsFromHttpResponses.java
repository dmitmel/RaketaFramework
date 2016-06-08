import github.dmitmel.raketaframework.web.HTTPResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class TestsForStringsFromHttpResponses extends Assert {
    @Test
    public void testNothing() {
        HTTPResponse message = new HTTPResponse("HTTP/1.1", 200, "OK");
        
        String expected = "HTTP/1.1 200 OK\r\n" +
                "\r\n";
        assertEquals(expected, message.toString());
    }
    
    @Test
    public void testHeadersAndBody() {
        HashMap<String, String> headers = new HashMap<>(2);
        headers.put("A", "B");
        headers.put("C", "D");
        HTTPResponse message = new HTTPResponse("HTTP/1.1", 200, "OK", headers, "Some text.");
        
        String expected = "HTTP/1.1 200 OK\r\n" +
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
        HTTPResponse message = new HTTPResponse("HTTP/1.1", 200, "OK", headers, "");
        
        String expected = "HTTP/1.1 200 OK\r\n" +
                "A: B\r\n" +
                "C: D\r\n" +
                "\r\n";
        assertEquals(expected, message.toString());
    }
    
    @Test
    public void testBody() {
        HTTPResponse message = new HTTPResponse("HTTP/1.1", 200, "OK", "Some text.");
        
        String expected = "HTTP/1.1 200 OK\r\n" +
                "\r\n" +
                "Some text.";
        assertEquals(expected, message.toString());
    }
}
