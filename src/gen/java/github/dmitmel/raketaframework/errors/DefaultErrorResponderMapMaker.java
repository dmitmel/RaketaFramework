package github.dmitmel.raketaframework.errors;

import java.util.Map;
import java.util.HashMap;

public class DefaultErrorResponderMapMaker {
    public static Map<Integer, ErrorResponder> makeMap() {
        HashMap<Integer, ErrorResponder> map = new HashMap<>(5);
        map.put(400, DefaultErrorResponderFactory.makeResponderForError(new Error400()));   // Bad Request
        map.put(404, DefaultErrorResponderFactory.makeResponderForError(new Error404()));   // Not Found
        map.put(405, DefaultErrorResponderFactory.makeResponderForError(new Error405()));   // Method Not Allowed
        map.put(500, DefaultErrorResponderFactory.makeResponderForError(new Error500()));   // Internal Server Error
        map.put(501, DefaultErrorResponderFactory.makeResponderForError(new Error501()));   // Not Implemented
        return map;
    }
}
