package github.dmitmel.raketaframework.web.errors;

import java.util.Map;
import java.util.HashMap;

public class DefaultErrorResponderMapMaker {
    public static Map<Integer, ErrorResponder> makeMap() {
        HashMap<Integer, ErrorResponder> map = new HashMap<>(5);
        map.put(400, DefaultErrorResponderFactory.makeResponder(new Error400()));   // Bad Request
        map.put(404, DefaultErrorResponderFactory.makeResponder(new Error404()));   // Not Found
        map.put(405, DefaultErrorResponderFactory.makeResponder(new Error405()));   // Method Not Allowed
        map.put(500, DefaultErrorResponderFactory.makeResponder(new Error500()));   // Internal Server Error
        map.put(501, DefaultErrorResponderFactory.makeResponder(new Error501()));   // Not Implemented
        return map;
    }
}
