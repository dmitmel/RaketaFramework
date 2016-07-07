package github.dmitmel.raketaframework.web.errors;

import java.util.Map;
import java.util.HashMap;

public class DefaultErrorResponderMapMaker {
    public static Map<Integer, ErrorResponder> makeMap() {
        HashMap<Integer, ErrorResponder> map = new HashMap<>(5);
        map.put(400, DefaultErrorResponderFactory.makeResponder());   // Bad Request
        map.put(404, DefaultErrorResponderFactory.makeResponder());   // Not Found
        map.put(405, DefaultErrorResponderFactory.makeResponder());   // Method Not Allowed
        map.put(500, DefaultErrorResponderFactory.makeResponder());   // Internal Server Error
        map.put(501, DefaultErrorResponderFactory.makeResponder());   // Not Implemented
        return map;
    }
}
