package github.dmitmel.raketaframework.exceptions;

/**
 * {@link java.lang.NoSuchMethodException}
 */
public class NoSuchMethodException extends ReflectiveOperationException {
    public NoSuchMethodException() {
    }

    public NoSuchMethodException(String message) {
        super(message);
    }

    public NoSuchMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchMethodException(Throwable cause) {
        super(cause);
    }

    public static NoSuchMethodException extractFrom(java.lang.NoSuchMethodException e) {
        return new NoSuchMethodException(e.getMessage(), e.getCause());
    }
}
