package org.willthisfly.raketaframework.errors;

public class DefaultErrorResponderFactory {
    private DefaultErrorResponderFactory() {
        throw new UnsupportedOperationException("Can\'t create instance of DefaultErrorResponderFactory");
    }
    

    public static ErrorResponder makeResponderForError(HTTPError target) {
        return target::toString;
    }
    
    public static ErrorResponder apply(HTTPError target) {
        return makeResponderForError(target);
    }
}
