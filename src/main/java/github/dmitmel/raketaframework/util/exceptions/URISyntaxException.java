package github.dmitmel.raketaframework.util.exceptions;

/**
 * {@link java.net.URISyntaxException}
 */
public class URISyntaxException extends RuntimeException {
    public URISyntaxException() {
    }

    public URISyntaxException(String message) {
        super(message);
    }

    public URISyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public URISyntaxException(Throwable cause) {
        super(cause);
    }

    public static URISyntaxException extractFrom(java.net.URISyntaxException e) {
        return new URISyntaxException(e.getMessage(), e.getCause());
    }
}
