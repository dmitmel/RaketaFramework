package org.willthisfly.raketaframework.routes;

import org.willthisfly.raketaframework.RequestParams;
import org.willthisfly.raketaframework.URI;
import org.willthisfly.raketaframework.util.Strings;

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
    
    @Override
    public MethodNotAllowedRoute clone() {
        try {
            return (MethodNotAllowedRoute) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException("MethodNotAllowedRoute.clone");
        }
    }
}
