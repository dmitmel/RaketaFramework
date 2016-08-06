package org.willthisfly.raketaframework;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class HTTPResponseSerializingTest {
    private static final String TESTING_MESSAGE_BODY = "Some text.";

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
        HTTPResponse message = new HTTPResponse("HTTP/1.1", 200, "OK", headers, TESTING_MESSAGE_BODY.getBytes());
        
        String expected = "HTTP/1.1 200 OK\r\n" +
                "A: B\r\n" +
                "C: D\r\n" +
                "\r\n" +
                TESTING_MESSAGE_BODY;
        assertEquals(expected, message.toString());
    }
    
    @Test
    public void testHeaders() {
        HashMap<String, String> headers = new HashMap<>(2);
        headers.put("A", "B");
        headers.put("C", "D");
        HTTPResponse message = new HTTPResponse("HTTP/1.1", 200, "OK", headers, new byte[0]);
        
        String expected = "HTTP/1.1 200 OK\r\n" +
                "A: B\r\n" +
                "C: D\r\n" +
                "\r\n";
        assertEquals(expected, message.toString());
    }
    
    @Test
    public void testBody() {
        HTTPResponse message = new HTTPResponse("HTTP/1.1", 200, "OK", TESTING_MESSAGE_BODY.getBytes());
        
        String expected = "HTTP/1.1 200 OK\r\n" +
                "\r\n" +
                TESTING_MESSAGE_BODY;
        assertEquals(expected, message.toString());
    }
}
