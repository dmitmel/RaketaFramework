package org.willthisfly.raketaframework.exceptions;

/**
 * {@link java.lang.InstantiationException}
 */
public class InstantiationException extends ReflectiveOperationException {
    public InstantiationException() {
    }

    public InstantiationException(String message) {
        super(message);
    }

    public InstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstantiationException(Throwable cause) {
        super(cause);
    }

    public static InstantiationException extractFrom(java.lang.InstantiationException e) {
        return new InstantiationException(e.getMessage(), e.getCause());
    }
}
