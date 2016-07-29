package org.willthisfly.raketaframework.tests.util;

import org.willthisfly.raketaframework.HTTPRequest;
import org.willthisfly.raketaframework.URI;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class HTTPRequestSerializingTest extends Assert {
    private static final String TESTING_MESSAGE_BODY = "Some text.";

    @Test
    public void testNothing() {
        HTTPRequest message = new HTTPRequest("GET", new URI("/"), "HTTP/1.1");

        String expected = "GET / HTTP/1.1\r\n" +
                "\r\n";
        assertEquals(expected, message.toString());
    }

    @Test
    public void testHeadersAndBody() {
        HashMap<String, String> headers = new HashMap<>(2);
        headers.put("A", "B");
        headers.put("C", "D");
        HTTPRequest message = new HTTPRequest("GET", new URI("/"), "HTTP/1.1", headers, TESTING_MESSAGE_BODY.getBytes());

        String expected = "GET / HTTP/1.1\r\n" +
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
        HTTPRequest message = new HTTPRequest("GET", new URI("/"), "HTTP/1.1", headers, new byte[0]);

        String expected = "GET / HTTP/1.1\r\n" +
                "A: B\r\n" +
                "C: D\r\n" +
                "\r\n";
        assertEquals(expected, message.toString());
    }

    @Test
    public void testBody() {
        HTTPRequest message = new HTTPRequest("GET", new URI("/"), "HTTP/1.1", TESTING_MESSAGE_BODY.getBytes());

        String expected = "GET / HTTP/1.1\r\n" +
                "\r\n" +
                TESTING_MESSAGE_BODY;
        assertEquals(expected, message.toString());
    }
}
