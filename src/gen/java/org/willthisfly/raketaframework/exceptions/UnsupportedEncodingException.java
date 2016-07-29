package org.willthisfly.raketaframework.exceptions;

/**
 * {@link java.io.UnsupportedEncodingException}
 */
public class UnsupportedEncodingException extends IOException {
    public UnsupportedEncodingException() {
    }

    public UnsupportedEncodingException(String message) {
        super(message);
    }

    public UnsupportedEncodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedEncodingException(Throwable cause) {
        super(cause);
    }

    public static UnsupportedEncodingException extractFrom(java.io.UnsupportedEncodingException e) {
        return new UnsupportedEncodingException(e.getMessage(), e.getCause());
    }
}
