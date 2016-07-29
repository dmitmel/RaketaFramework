package github.dmitmel.raketaframework.exceptions;

/**
 * {@link java.lang.ReflectiveOperationException}
 */
public class ReflectiveOperationException extends RuntimeException {
    public ReflectiveOperationException() {
    }

    public ReflectiveOperationException(String message) {
        super(message);
    }

    public ReflectiveOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectiveOperationException(Throwable cause) {
        super(cause);
    }

    public static ReflectiveOperationException extractFrom(java.lang.ReflectiveOperationException e) {
        return new ReflectiveOperationException(e.getMessage(), e.getCause());
    }
}
