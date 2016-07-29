package github.dmitmel.raketaframework.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Maps {
    private Maps() {
        throw new RuntimeException("Can\'t create instance of Maps");
    }
    
    
    @SafeVarargs
    public static <K, V> Map<K, V> unmodifiableMap(Map.Entry<K, V>... entries) {
        HashMap<K, V> map = new HashMap<>();
        for (Map.Entry<K, V> entry : entries)
            map.put(entry.getKey(), entry.getValue());
        return Collections.unmodifiableMap(map);
    }
}
