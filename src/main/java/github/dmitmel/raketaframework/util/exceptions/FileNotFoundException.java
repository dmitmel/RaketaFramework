package github.dmitmel.raketaframework.util.exceptions;

/**
 * {@link java.io.FileNotFoundException}
 */
public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException() {
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNotFoundException(Throwable cause) {
        super(cause);
    }

    public static InvocationTargetException extractFrom(java.io.FileNotFoundException e) {
        return new InvocationTargetException(e.getMessage(), e.getCause());
    }
}
