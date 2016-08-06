package org.willthisfly.raketaframework;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class URIParsingTest {
    @Test
    public void testPathToFile() {
        doClassicTest("/a/b/c.d");
    }

    @Test
    public void testPathToFileAndOneParam() {
        doClassicTest("/a/b/c.d?key=value");
    }

    @Test
    public void testPathToFileAndMultipleParams() {
        doClassicTest("/a/b/c.d?key1=value1&key2=value2");
    }

    @Test
    public void testPathToFileAndAnchor() {
        doClassicTest("/a/b/c.d#article");
    }

    @Test
    public void testPathToFileAndOneParamAndAnchor() {
        doClassicTest("/a/b/c.d#article?key=value");
    }

    @Test
    public void testPathToFileAndMultipleParamsAndAnchor() {
        doClassicTest("/a/b/c.d#article?key1=value1&key2=value2");
    }


    private void doClassicTest(String expected) {
        URI actual = URI.fromString(expected);
        assertEquals(expected, actual.toString());
        assertEquals(expected.length(), actual.length());
    }
    
    
    @Test
    public void testEmptyAnchorAndThenParams() {
        URI uri = URI.fromString("/#?");
        assertEquals(uri.anchor, URI.EMPTY_ANCHOR);
        assertEquals(uri.params, URI.EMPTY_PARAMS);
    }

    @Test
    public void testOnlyAnchorStarter() {
        URI uri = URI.fromString("/a/b/c.d#");
        assertEquals(uri.anchor, URI.EMPTY_ANCHOR);
    }

    @Test
    public void testOnlyParamsStarter() {
        URI uri = URI.fromString("/a/b/c.d?");
        assertEquals(uri.params.size(), 0);
    }

    @Test
    public void testOneParamDefinitionWithoutValue() {
        URI uri = URI.fromString("/a/b/c.d?key=");
        assertEquals(uri.params.get("key"), URI.EMPTY_PARAM_VALUE);
    }
    
    @Test
    public void testOneParamDefinitionWithoutEqualsSign() {
        URI uri = URI.fromString("/a/b/c.d?key");
        assertEquals(uri.params.get("key"), URI.EMPTY_PARAM_VALUE);
    }
    
    @Test
    public void testOneParamDefinitionWithoutMultipleEqualsSigns() {
        URI uri = URI.fromString("/a/b/c.d?key=a=b");
        assertEquals(uri.params.get("key"), "a=b");
    }

    @Test
    public void testMultipleParamsDefinitionWithoutValue() {
        URI uri = URI.fromString("/a/b/c.d?key1=&key2=");
        assertEquals(uri.params.get("key1"), URI.EMPTY_PARAM_VALUE);
        assertEquals(uri.params.get("key2"), URI.EMPTY_PARAM_VALUE);
    }
    
    @Test
    public void testMultipleParamsDefinitionWithoutEqualsSign() {
        URI uri = URI.fromString("/a/b/c.d?key1&key2");
        assertEquals(uri.params.get("key1"), URI.EMPTY_PARAM_VALUE);
        assertEquals(uri.params.get("key2"), URI.EMPTY_PARAM_VALUE);
    }
    
    @Test
    public void testMultipleParamsDefinitionWithMultipleEqualsSigns() {
        URI uri = URI.fromString("/a/b/c.d?key1=a=b&key2=c=d");
        assertEquals(uri.params.get("key1"), "a=b");
        assertEquals(uri.params.get("key2"), "c=d");
    }
}
