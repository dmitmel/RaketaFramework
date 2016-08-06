package org.willthisfly.raketaframework.routes;

import org.willthisfly.raketaframework.RequestParams;
import org.willthisfly.raketaframework.URI;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface Route extends Cloneable {
    boolean allowsURI(URI uri);
    
    default boolean allowsMethod(String method) {
        return getMethod().equals(method);
    }
    
    String getMethod();
    
    default List<String> getCapturesForURI(URI uri) {
        return Collections.emptyList();
    }
    
    default Map<String, String> getNamedCapturesForURI(URI uri) {
        return Collections.emptyMap();
    }
    
    Object handleRequest(RequestParams params);
    
    Route clone();
}
