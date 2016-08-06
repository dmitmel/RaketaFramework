package org.willthisfly.raketaframework;

import org.willthisfly.raketaframework.util.ExtendedComparator;
import org.willthisfly.raketaframework.util.JSONJavaSerializer;

import java.util.*;

public class RequestParams extends HashMap<String, Object> implements Comparable<RequestParams>, Cloneable {
    public RequestParams(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }
    
    public RequestParams(int initialCapacity) {
        super(initialCapacity);
    }
    
    public RequestParams() {}
    
    public RequestParams(Map<? extends String, ?> m) {
        super(m);
    }
    
    public RequestParams(HTTPRequest request) {
        putAll(request.uri.params);
        put("request", request);
    
        String contentType = request.headers.get("Content-Type");
        if (MIMETypes.FORM.equals(contentType))
            // Form's format is similar to URL's params format, so I use parsing method from URI
            putAll(URI.parseParams(URI.decode(request.bodyToString())));
    }
    
    
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        Object object = super.get(key);
        return (T) object;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getOrElse(String key, T orElse) {
        return containsKey(key) ? (T) get(key) : orElse;
    }
    
    public List<String> getCaptures() {
        return getOrElse("captures", Collections.emptyList());
    }
    
    public Map<String, String> getNamedCaptures() {
        return getOrElse("named-captures", Collections.emptyMap());
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
    
    public HTTPRequest getRequest() {
        return get("request");
    }
    
    
    @Override
    public String toString() {
        return JSONJavaSerializer.toMultilineJSON(this);
    }
    
    @Override
    public int compareTo(RequestParams that) {
        return ExtendedComparator.compareByKeys(this, that);
    }
    
    @Override
    public RequestParams clone() {
        return (RequestParams) super.clone();
    }
}
