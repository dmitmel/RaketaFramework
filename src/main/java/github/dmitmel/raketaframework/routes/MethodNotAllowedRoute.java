package github.dmitmel.raketaframework.routes;

import github.dmitmel.raketaframework.URI;
import github.dmitmel.raketaframework.app.RequestParams;
import github.dmitmel.raketaframework.util.Strings;

public class MethodNotAllowedRoute implements Route {
    @Override
    public boolean allowsURI(URI uri) {
        return true;
    }
    
    @Override
    public String getMethod() {
        // Returning empty string in this method does effect of overriding method Route#allowsMethod
        return Strings.EMPTY_STRING;
    }
    
    @Override
    public Object handleRequest(RequestParams params) {
        throw new UnsupportedOperationException("MethodNotAllowedRoute.handleRequest");
    }
}
