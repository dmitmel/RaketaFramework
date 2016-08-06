package org.willthisfly.raketaframework.util;

import java.util.*;
import java.util.Arrays;

public class JSONJavaDSL {
    private JSONJavaDSL() {
        throw new RuntimeException("Can\'t create instance of JSONJavaDSL");
    }
    
    
    public static JSONTree json(JSONEntry... entries) {
        return new JSONTree(map(entries));
    }
    
    public static JSONMap map(JSONEntry... entries) {
        return new JSONMap(Maps.unmodifiableMap(entries));
    }
    
    public static JSONEntry entry(String key, Object value) {
        return new JSONEntry(key, value);
    }

    @SafeVarargs
    public static <T> JSONList<T> list(T... items) {
        return new JSONList<>(Arrays.asList(items));
    }
    
    /////////////////////// Type-aliases for JSON DSL ///////////////////////
    public static class JSONTree extends JSONMap {
        public JSONTree(int initialCapacity, float loadFactor) { super(initialCapacity, loadFactor); }
        public JSONTree(int initialCapacity) { super(initialCapacity); }
        public JSONTree() {}
        public JSONTree(Map<? extends String, ?> m) { super(m); }
    }
    
    public static class JSONMap extends HashMap<String, Object> {
        public JSONMap(int initialCapacity, float loadFactor) { super(initialCapacity, loadFactor); }
        public JSONMap(int initialCapacity) { super(initialCapacity); }
        public JSONMap() {}
        public JSONMap(Map<? extends String, ?> m) { super(m); }
    }
    
    public static class JSONEntry extends SimpleEntry<String, Object> {
        public JSONEntry(String key, Object value) { super(key, value); }
    }
    
    public static class JSONList<T> extends ArrayList<T> {
        public JSONList(int initialCapacity) { super(initialCapacity); }
        public JSONList() {}
        public JSONList(Collection<? extends T> c) { super(c); }
    }
}
