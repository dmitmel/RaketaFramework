package org.willthisfly.raketaframework.tests.util;

import org.willthisfly.raketaframework.util.JSONJavaDSL;
import org.willthisfly.raketaframework.util.JSONSerializer;
import org.junit.Assert;
import org.junit.Test;

import static org.willthisfly.raketaframework.util.JSONJavaDSL.*;

public class JSONSingleLineSerializingTest extends Assert {
    @Test
    public void testNumbers() {
        JSONJavaDSL.JSONTree tree =
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
        assertEquals(expected, JSONSerializer.toSingleLineJSON(tree));
    }
    
    @Test
    public void testBooleans() {
        JSONJavaDSL.JSONTree tree =
                json(
                    entry("JSON can contain single-quoted strings", false),
                    entry("JSON can contain numbers", true)
                );
        String expected =
                "{" +
                "\"JSON can contain single-quoted strings\": false, " +
                "\"JSON can contain numbers\": true" +
                "}";
        assertEquals(expected, JSONSerializer.toSingleLineJSON(tree));
    }
    
    @Test
    public void testChars() {
        JSONJavaDSL.JSONTree tree =
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
        assertEquals(expected, JSONSerializer.toSingleLineJSON(tree));
    }
    
    @Test
    public void testNestedMaps() {
        JSONJavaDSL.JSONTree tree =
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
        assertEquals(expected, JSONSerializer.toSingleLineJSON(tree));
    }
    
    @Test
    public void testEmptyMaps() {
        JSONJavaDSL.JSONTree tree =
                json(
                    entry("empty-map", map())
                );
        String expected =
                "{\"empty-map\": {}}";
        assertEquals(expected, JSONSerializer.toSingleLineJSON(tree));
    }
    
    @Test
    public void testLists() {
        JSONJavaDSL.JSONTree tree =
                json(
                    entry("list", list(1, 2, 3))
                );
        String expected =
                "{\"list\": [1, 2, 3]}";
        assertEquals(expected, JSONSerializer.toSingleLineJSON(tree));
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
        assertEquals(expected, JSONSerializer.toSingleLineJSON(tree));
    }
}
