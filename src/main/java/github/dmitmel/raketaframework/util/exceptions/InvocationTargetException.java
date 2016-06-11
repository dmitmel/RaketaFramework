package github.dmitmel.raketaframework.util.exceptions;

/**
 * {@link java.lang.reflect.InvocationTargetException}
 */
public class InvocationTargetException extends RuntimeException {
    public InvocationTargetException(String message) {
        super(message);
    }

    public InvocationTargetException() {
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
