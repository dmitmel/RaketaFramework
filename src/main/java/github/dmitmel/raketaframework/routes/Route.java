package github.dmitmel.raketaframework.routes;

import github.dmitmel.raketaframework.URI;
import github.dmitmel.raketaframework.app.RequestParams;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface Route extends Cloneable {
    boolean allowsURI(URI uri);
    default boolean allowsMethod(String method) {
        return getMethod().contains(method);
    }
    
    String getMethod();
    
    default List<String> getCapturesForURI(URI uri) {
        return Collections.emptyList();
    }
    
    default Map<String, String> getNamedCapturesForURI(URI uri) {
        return Collections.emptyMap();
    }
    
    Object handleRequest(RequestParams params);
}
