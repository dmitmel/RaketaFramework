package org.willthisfly.raketaframework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONJavaSerializer {
    private static final String DEFAULT_INDENT = "    ";    // 4x spaces
    
    
    private JSONJavaSerializer() {
        throw new RuntimeException("Can\'t create instance of JSONJavaSerializer");
    }
    
    
    public static <K, V> String toMultilineJSON(Map<K, V> map) {
        return toJSONWithIndent(DEFAULT_INDENT, map);
    }
    
    public static <K, V> String toSingleLineJSON(Map<K, V> map) {
        return toJSONWithIndent(Strings.EMPTY_STRING, map);
    }
    
    public static <K, V> String toJSONWithIndent(String indentBase, Map<K, V> map) {
        return mapToJSON(indentBase, 0, map);
    }
    
    @SuppressWarnings("unchecked")
    private static <T> String objectToJSON(String indentBase, int depth, T value) {
        if (value instanceof Map) {
            return mapToJSON(indentBase, depth, (Map<String, Object>) value);
        } else if (value instanceof Iterable) {
            return iterableToJSON(indentBase, depth, (Iterable<Object>) value);
        } else if (value instanceof Boolean || value instanceof Number) {
            return value.toString();
        } else {
            return String.format("\"%s\"", value);
        }
    }
    
    private static <K, V> String mapToJSON(String indentBase, int depth, Map<K, V> map) {
        StringBuilder json = new StringBuilder();
        boolean useIndent = !indentBase.isEmpty();
    
        int nextDepth = depth + 1;
        String nextIndent = Strings.copyString(indentBase, nextDepth);
        
        json.append('{');
        
        if (useIndent) {
            if (!map.isEmpty()) {
                json.append('\n');
                
                List<String> lines = map.entrySet().stream()
                        .map(entry -> String.format("%s\"%s\": %s", nextIndent, entry.getKey(),
                                objectToJSON(indentBase, nextDepth, entry.getValue())))
                        .collect(Collectors.toList());
                json.append(String.join(",\n", lines));
                
                json.append('\n');
                json.append(Strings.copyString(indentBase, depth));
            }
        } else {
            List<String> stringEntries = map.entrySet().stream()
                    .map(entry -> String.format("\"%s\": %s", entry.getKey(),
                            objectToJSON(indentBase, nextDepth, entry.getValue())))
                    .collect(Collectors.toList());
            json.append(String.join(", ", stringEntries));
        }
        
        json.append('}');
        
        return json.toString();
    }
    
    private static <T> String iterableToJSON(String indentBase, int depth, Iterable<T> value) {
        StringBuilder json = new StringBuilder();
        boolean useIndent = !indentBase.isEmpty();
        List<T> list = Lists.fromIterable(value);
    
        int nextDepth = depth + 1;
        String nextIndent = Strings.copyString(indentBase, nextDepth);
    
        json.append('[');
    
        if (useIndent) {
            if (!list.isEmpty()) {
                json.append('\n');
    
                ArrayList<String> lines = list.stream()
                        .map(item -> nextIndent + objectToJSON(indentBase, nextDepth, item))
                        .collect(Collectors.toCollection(ArrayList::new));
    
                json.append(String.join(",\n", lines));
            
                json.append('\n');
                json.append(Strings.copyString(indentBase, depth));
            }
        } else {
            List<String> stringList = list.stream()
                    .map(t -> objectToJSON(indentBase, nextDepth, t))
                    .collect(Collectors.toList());
            json.append(String.join(", ", stringList));
        }
    
        json.append(']');
    
        return json.toString();
    }
}
