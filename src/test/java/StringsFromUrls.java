import github.dmitmel.raketaframework.web.URL;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class StringsFromUrls extends Assert {
    @Test
    public void testFile() {
        URL url = new URL("/a/b/c.d");
        assertEquals("/a/b/c.d", url.toString());
    }

    @Test
    public void testFileAndOneParam() {
        HashMap<String, String> params = new HashMap<>(0);
        params.put("key", "value");
        URL url = new URL("/a/b/c.d", params);
        assertEquals("/a/b/c.d?key=value", url.toString());
    }

    @Test
    public void testFileAndMultipleParams() {
        HashMap<String, String> params = new HashMap<>(0);
        params.put("key1", "value1");
        params.put("key2", "value2");
        URL url = new URL("/a/b/c.d", params);
        assertEquals("/a/b/c.d?key1=value1&key2=value2", url.toString());
    }

    @Test
    public void testFileAndAnchor() {
        URL url = new URL("/a/b/c.d", "article");
        assertEquals("/a/b/c.d#article", url.toString());
    }

    @Test
    public void testFileAndOneParamAndAnchor() {
        HashMap<String, String> params = new HashMap<>(0);
        params.put("key", "value");
        URL url = new URL("/a/b/c.d", "article", params);
        assertEquals("/a/b/c.d#article?key=value", url.toString());
    }

    @Test
    public void testFileAndMultipleParamsAndAnchor() {
        HashMap<String, String> params = new HashMap<>(0);
        params.put("key1", "value1");
        params.put("key2", "value2");
        URL url = new URL("/a/b/c.d", "article", params);
        assertEquals("/a/b/c.d#article?key1=value1&key2=value2", url.toString());
    }
}
