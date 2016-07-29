package github.dmitmel.raketaframework.tests.util;

import github.dmitmel.raketaframework.URI;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class URISerializingTest extends Assert {
    @Test
    public void testFile() {
        URI uri = new URI("/a/b/c.d");
        assertEquals("/a/b/c.d", uri.toString());
    }

    @Test
    public void testFileAndOneParam() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", "value");
        URI uri = new URI("/a/b/c.d", params);
        assertEquals("/a/b/c.d?key=value", uri.toString());
    }

    @Test
    public void testFileAndMultipleParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");
        URI uri = new URI("/a/b/c.d", params);
        assertEquals("/a/b/c.d?key1=value1&key2=value2", uri.toString());
    }

    @Test
    public void testFileAndAnchor() {
        URI uri = new URI("/a/b/c.d", "article");
        assertEquals("/a/b/c.d#article", uri.toString());
    }

    @Test
    public void testFileAndOneParamAndAnchor() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", "value");
        URI uri = new URI("/a/b/c.d", "article", params);
        assertEquals("/a/b/c.d#article?key=value", uri.toString());
    }

    @Test
    public void testFileAndMultipleParamsAndAnchor() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");
        URI uri = new URI("/a/b/c.d", "article", params);
        assertEquals("/a/b/c.d#article?key1=value1&key2=value2", uri.toString());
    }
}
