import github.dmitmel.raketaframework.web.HTTPResponse;
import org.junit.Assert;
import org.junit.Test;

public class TestsForHttpResponsesFromStrings extends Assert {
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
                "Some text.";
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
                "Some text.";
        doTest(expected);
    }

    private void doTest(String expected) {
        HTTPResponse actual = HTTPResponse.fromRequestString(expected);
        assertEquals(expected, actual.toString());
    }
}
