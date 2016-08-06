package org.willthisfly.raketaframework.exceptions;

/**
 * {@link java.lang.reflect.InvocationTargetException}
 */
public class InvocationTargetException extends ReflectiveOperationException {
    public InvocationTargetException() {
    }

    public InvocationTargetException(String message) {
        super(message);
    }

    public InvocationTargetException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvocationTargetException(Throwable cause) {
        super(cause);
    }

    public static InvocationTargetException extractFrom(java.lang.reflect.InvocationTargetException e) {
        return new InvocationTargetException(e.getMessage(), e.getCause());
    }
}
