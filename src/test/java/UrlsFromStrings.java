import github.dmitmel.raketaframework.web.URL;
import org.junit.Assert;
import org.junit.Test;

public class UrlsFromStrings extends Assert {
    @Test
    public void testPathToFile() {
        doTest("/a/b/c.d");
    }

    @Test
    public void testPathToFileAndOneParam() {
        doTest("/a/b/c.d?key=value");
    }

    @Test
    public void testPathToFileAndMultipleParams() {
        doTest("/a/b/c.d?key1=value1&key2=value2");
    }

    @Test
    public void testPathToFileAndAnchor() {
        doTest("/a/b/c.d#article");
    }

    @Test
    public void testPathToFileAndOneParamAndAnchor() {
        doTest("/a/b/c.d#article?key=value");
    }

    @Test
    public void testPathToFileAndMultipleParamsAndAnchor() {
        doTest("/a/b/c.d#article?key1=value1&key2=value2");
    }


    private void doTest(String expected) {
        URL actual = URL.fromString(expected);
        assertEquals(expected, actual.toString());
    }


    @Test
    public void testOnlyAnchorStarter() {
        URL url = URL.fromString("/a/b/c.d#");
        assertEquals(url.anchor, URL.EMPTY_ANCHOR);
    }

    @Test
    public void testOnlyParamsStarter() {
        URL url = URL.fromString("/a/b/c.d?");
        assertEquals(url.params.size(), 0);
    }

    @Test
    public void testOneParamDefinitionWithoutValue() {
        URL url = URL.fromString("/a/b/c.d?key=");
        assertEquals(url.params.get("key"), "");
    }

    @Test
    public void testMultipleParamsDefinitionWithoutValue() {
        URL url = URL.fromString("/a/b/c.d?key1=&key2=");
        assertEquals(url.params.get("key1"), "");
        assertEquals(url.params.get("key2"), "");
    }
}
