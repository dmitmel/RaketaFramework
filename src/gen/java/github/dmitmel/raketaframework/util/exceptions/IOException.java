package github.dmitmel.raketaframework.util.exceptions;

/**
 * {@link java.io.IOException}
 */
public class IOException extends RuntimeException {
    public IOException() {
    }

    public IOException(String message) {
        super(message);
    }

    public IOException(String message, Throwable cause) {
        super(message, cause);
    }

    public IOException(Throwable cause) {
        super(cause);
    }

    public static IOException extractFrom(java.io.IOException e) {
        return new IOException(e.getMessage(), e.getCause());
    }
}
