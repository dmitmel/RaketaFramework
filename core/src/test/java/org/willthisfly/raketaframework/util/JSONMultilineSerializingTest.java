package org.willthisfly.raketaframework.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.willthisfly.raketaframework.util.JSONJavaDSL.*;

public class JSONMultilineSerializingTest {
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
                "{\n" +
                "    \"byte\": 0,\n" +
                "    \"double\": 5.0,\n" +
                "    \"short\": 1,\n" +
                "    \"float\": 4.0,\n" +
                "    \"int\": 2,\n" +
                "    \"long\": 3\n" +
                "}";
        assertEquals(expected, JSONJavaSerializer.toMultilineJSON(tree));
    }
    
    @Test
    public void testBooleans() {
        JSONTree tree =
                json(
                    entry("JSON can contain single-quoted strings", false),
                    entry("JSON can contain numbers", true)
                );
        String expected =
                "{\n" +
                "    \"JSON can contain single-quoted strings\": false,\n" +
                "    \"JSON can contain numbers\": true\n" +
                "}";
        assertEquals(expected, JSONJavaSerializer.toMultilineJSON(tree));
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
                "{\n" +
                "    \"charA\": \"a\",\n" +
                "    \"charC\": \"c\",\n" +
                "    \"charB\": \"b\"\n" +
                "}";
        assertEquals(expected, JSONJavaSerializer.toMultilineJSON(tree));
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
                "{\n" +
                "    \"outer\": {\n" +
                "        \"outerValue\": 1,\n" +
                "        \"inner\": {\n" +
                "            \"innerValue\": 2\n" +
                "        }\n" +
                "    }\n" +
                "}";
        assertEquals(expected, JSONJavaSerializer.toMultilineJSON(tree));
    }
    
    @Test
    public void testEmptyMaps() {
        JSONTree tree =
            json(
                entry("empty-map", map())
            );
        String expected =
                "{\n" +
                "    \"empty-map\": {}\n" +
                "}";
        assertEquals(expected, JSONJavaSerializer.toMultilineJSON(tree));
    }
    
    @Test
    public void testLists() {
        JSONTree tree =
                json(
                    entry("list", list(1, 2, 3))
                );
        String expected =
                "{\n" +
                "    \"list\": [\n" +
                "        1,\n" +
                "        2,\n" +
                "        3\n" +
                "    ]\n" +
                "}";
        assertEquals(expected, JSONJavaSerializer.toMultilineJSON(tree));
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
                "{\n" +
                "    \"outer\": [\n" +
                "        \"outerValue\",\n" +
                "        [\n" +
                "            \"innerValue\"\n" +
                "        ]\n" +
                "    ]\n" +
                "}";
        assertEquals(expected, JSONJavaSerializer.toMultilineJSON(tree));
    }
}
