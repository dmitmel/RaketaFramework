package org.willthisfly.raketaframework.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.willthisfly.raketaframework.util.JSONJavaDSL.*;

public class JSONSingleLineSerializingTest {
    @Test
    public void testNumbers() {
        JSONTree tree =
                json(
                    entry("byte", (byte) 0),
                    entry("double", 5.0),
                    entry("short", (short) 1),
                    entry("float", 4f),
                    entry("int", 2),
                    entry("long", 3L)
                );
        String expected =
                "{" +
                "\"byte\": 0, " +
                "\"double\": 5.0, " +
                "\"short\": 1, " +
                "\"float\": 4.0, " +
                "\"int\": 2, " +
                "\"long\": 3" +
                "}";
        assertEquals(expected, JSONJavaSerializer.toSingleLineJSON(tree));
    }
    
    @Test
    public void testBooleans() {
        JSONTree tree =
                json(
                    entry("JSON can contain single-quoted strings", false),
                    entry("JSON can contain numbers", true)
                );
        String expected =
                "{" +
                "\"JSON can contain single-quoted strings\": false, " +
                "\"JSON can contain numbers\": true" +
                "}";
        assertEquals(expected, JSONJavaSerializer.toSingleLineJSON(tree));
    }
    
    @Test
    public void testChars() {
        JSONTree tree =
                json(
                    entry("charA", 'a'),
                    entry("charC", 'c'),
                    entry("charB", 'b')
                );
        String expected =
                "{" +
                "\"charA\": \"a\", " +
                "\"charC\": \"c\", " +
                "\"charB\": \"b\"" +
                "}";
        assertEquals(expected, JSONJavaSerializer.toSingleLineJSON(tree));
    }
    
    @Test
    public void testNestedMaps() {
        JSONTree tree =
                json(
                    entry("outer", map(
                        entry("outerValue", 1),
                        entry("inner", map(
                            entry("innerValue", 2)
                        ))
                    ))
                );
        String expected =
                "{\"outer\": {\"outerValue\": 1, \"inner\": {\"innerValue\": 2}}}";
        assertEquals(expected, JSONJavaSerializer.toSingleLineJSON(tree));
    }
    
    @Test
    public void testEmptyMaps() {
        JSONTree tree =
                json(
                    entry("empty-map", map())
                );
        String expected =
                "{\"empty-map\": {}}";
        assertEquals(expected, JSONJavaSerializer.toSingleLineJSON(tree));
    }
    
    @Test
    public void testLists() {
        JSONTree tree =
                json(
                    entry("list", list(1, 2, 3))
                );
        String expected =
                "{\"list\": [1, 2, 3]}";
        assertEquals(expected, JSONJavaSerializer.toSingleLineJSON(tree));
    }
    
    @Test
    public void testNestedLists() {
        JSONTree tree =
                json(
                    entry("outer", list(
                        "outerValue",
                        list("innerValue")
                    ))
                );
        String expected =
                "{\"outer\": [\"outerValue\", [\"innerValue\"]]}";
        assertEquals(expected, JSONJavaSerializer.toSingleLineJSON(tree));
    }
}
