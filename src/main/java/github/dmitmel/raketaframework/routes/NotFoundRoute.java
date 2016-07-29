package github.dmitmel.raketaframework.routes;

import github.dmitmel.raketaframework.URI;
import github.dmitmel.raketaframework.app.RequestParams;
import github.dmitmel.raketaframework.util.Strings;

public class NotFoundRoute implements Route {
    @Override
    public boolean allowsURI(URI uri) {
        return false;
    }
    
    @Override
    public String getMethod() {
        return Strings.EMPTY_STRING;
    }
    
    @Override
    public Object handleRequest(RequestParams params) {
        throw new UnsupportedOperationException("NotFoundRoute.handleRequest");
    }
}
