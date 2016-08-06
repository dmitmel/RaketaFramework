package org.willthisfly.raketaframework.routes;

import org.willthisfly.raketaframework.HTTPRequest;
import org.willthisfly.raketaframework.URI;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Router implements Iterable<Route> {
    public abstract List<Route> getRoutes();
    public abstract Router addAll(Router other);
    
    public List<Route> routesForURI(URI uri) {
        return getRoutes().stream()
                .filter(route -> route.allowsURI(uri))
                .collect(Collectors.toList());
    }
    
    public List<String> methodsForURI(URI uri) {
        List<Route> available = routesForURI(uri);
        return available.stream()
                .map(Route::getMethod)
                .collect(Collectors.toList());
    }
    
    public Route routeForHTTPRequest(HTTPRequest httpRequest) {
        List<Route> availableForURI = routesForURI(httpRequest.uri);
        
        if (availableForURI.isEmpty()) {
            return new NotFoundRoute();
        } else {
            List<Route> availableForMethod = availableForURI.stream()
                    .filter(route -> route.allowsMethod(httpRequest.method))
                    .collect(Collectors.toList());
            
            if (availableForMethod.isEmpty())
                return new MethodNotAllowedRoute();
            else
                return availableForMethod.get(0);
        }
    }
    
    @Override
    public Iterator<Route> iterator() {
        return getRoutes().iterator();
    }
}
