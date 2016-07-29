package org.willthisfly.raketaframework.routes;

import org.willthisfly.raketaframework.URI;
import org.willthisfly.raketaframework.app.RequestParams;
import org.willthisfly.raketaframework.util.Strings;

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
