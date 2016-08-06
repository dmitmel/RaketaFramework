package org.willthisfly.raketaframework;

import org.junit.Test;
import org.willthisfly.raketaframework.util.SyntaxException;

import static org.junit.Assert.assertEquals;

public class HTTPResponseParsingTest {
    @Test
    public void testNothing() {
        String expected = "HTTP/1.1 200 OK\r\n" +
                "\r\n";
        doTest(expected);
    }

    @Test
    public void testHeadersAndBody() {
        String expected = "HTTP/1.1 200 OK\r\n" +
                "A: B\r\n" +
                "C: D\r\n" +
                "\r\n" +
                "Some text.\r\n";
        doTest(expected);
    }

    @Test
    public void testHeaders() {
        String expected = "HTTP/1.1 200 OK\r\n" +
                "A: B\r\n" +
                "C: D\r\n" +
                "\r\n";
        doTest(expected);
    }

    @Test
    public void testBody() {
        String expected = "HTTP/1.1 200 OK\r\n" +
                "\r\n" +
                "Some text.\r\n";
        doTest(expected);
    }
    
    @Test(expected = SyntaxException.class)
    public void testNoProtocol() {
        HTTPResponse.fromString("");
    }
    
    @Test(expected = SyntaxException.class)
    public void testNoStatusCode() {
        HTTPResponse.fromString("HTTP/1.1");
    }
    
    @Test(expected = SyntaxException.class)
    public void testNoStatusDescription() {
        HTTPResponse.fromString("HTTP/1.1 200");
    }

    private void doTest(String expected) {
        HTTPResponse actual = HTTPResponse.fromString(expected);
        assertEquals(expected, actual.toString());
        assertEquals(expected.length(), actual.length());
    }
}
