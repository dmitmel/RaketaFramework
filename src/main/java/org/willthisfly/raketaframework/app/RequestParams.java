package org.willthisfly.raketaframework.app;

import org.willthisfly.raketaframework.HTTPRequest;
import org.willthisfly.raketaframework.MIMETypes;
import org.willthisfly.raketaframework.URI;
import org.willthisfly.raketaframework.util.ExtendedComparator;
import org.willthisfly.raketaframework.util.JSONSerializer;

import java.util.*;

public class RequestParams extends HashMap<String, Object> implements Comparable<RequestParams>, Cloneable {
    private static final HTTPRequest NULL_REQUEST = new HTTPRequest("GET", new URI("/"), "HTTP/1.1");
    
    
    public RequestParams(HTTPRequest request) {
        super.putAll(request.uri.params);
        super.put("request", request);
    
        String contentType = request.headers.get("Content-Type");
        if (MIMETypes.FORM.equals(contentType))
            // Form's format is similar to URL's params format, so I use parsing method from URI
            super.putAll(URI.parseParams(URI.decode(request.bodyToString())));
    }
    
    
    // Making map unmodifiable for others
    @Override
    public Object put(String key, Object value) {
        throw new UnsupportedOperationException("RequestParams.put");
    }
    
    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException("RequestParams.remove");
    }
    
    @Override
    public void putAll(Map<? extends String, ?> map) {
        throw new UnsupportedOperationException("RequestParams.putAll");
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException("RequestParams.clear");
    }
    
    
    @SuppressWarnings("unchecked")
    public List<String> getCaptures() {
        Object captures = get("captures");
        
        if (captures instanceof List)
            return (List<String>) captures;
        
        return Collections.emptyList();
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, String> getNamedCaptures() {
        Object namedCaptures = get("named-captures");
        
        if (namedCaptures instanceof Map)
            return (Map<String, String>) namedCaptures;
        
        return Collections.emptyMap();
    }
    
    public String getCapture(int i) {
        return getCaptures().get(i);
    }
    
    public String getCaptureOrElse(int i, String orElse) {
        try {
            return getCapture(i);
        } catch (ArrayIndexOutOfBoundsException e) {
            return orElse;
        }
    }
    
    public void setupCaptures(List<String> captures, Map<String, String> namedCaptures) {
        super.putAll(namedCaptures);
        super.put("named-captures", namedCaptures);
        
        if (getCaptures().isEmpty())
            super.put("captures", Collections.unmodifiableList(new ArrayList<>(captures)));
        else
            throw new UnsupportedOperationException("RequestParams.setupCaptures");
    }
    
    
    @SuppressWarnings("unchecked")
    public HTTPRequest getRequest() {
        Object request = get("request");
        
        if (request instanceof HTTPRequest)
            return (HTTPRequest) request;
        
        return NULL_REQUEST;
    }
    
    
    public String getString(String key) {
        return (String) get(key);
    }
    
    public String getStringOrElse(String key, String orElse) {
        String string = getString(key);
        return string == null ? orElse : string;
    }
    
    
    @Override
    public String toString() {
        return JSONSerializer.toMultilineJSON(this);
    }
    
    @Override
    public int compareTo(RequestParams that) {
        return ExtendedComparator.compareByKeys(this, that);
    }
}
