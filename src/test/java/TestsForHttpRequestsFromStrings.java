import github.dmitmel.raketaframework.web.HTTPRequest;
import org.junit.Assert;
import org.junit.Test;

public class TestsForHttpRequestsFromStrings extends Assert {
    @Test
    public void testNothing() {
        String expected = "GET / HTTP/1.1\r\n" +
                "\r\n";
        doTest(expected);
    }

    @Test
    public void testHeadersAndBody() {
        String expected = "GET / HTTP/1.1\r\n" +
                "A: B\r\n" +
                "C: D\r\n" +
                "\r\n" +
                "Some text.";
        doTest(expected);
    }

    @Test
    public void testHeaders() {
        String expected = "GET / HTTP/1.1\r\n" +
                "A: B\r\n" +
                "C: D\r\n" +
                "\r\n";
        doTest(expected);
    }

    @Test
    public void testBody() {
        String expected = "GET / HTTP/1.1\r\n" +
                "\r\n" +
                "Some text.";
        doTest(expected);
    }

    private void doTest(String expected) {
        HTTPRequest actual = HTTPRequest.fromRequestString(expected);
        assertEquals(expected, actual.toString());
    }
}
