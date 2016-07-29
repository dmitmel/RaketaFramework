package github.dmitmel.raketaframework.exceptions;

/**
 * {@link java.io.FileNotFoundException}
 */
public class FileNotFoundException extends IOException {
    public FileNotFoundException() {
    }

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNotFoundException(Throwable cause) {
        super(cause);
    }

    public static FileNotFoundException extractFrom(java.io.FileNotFoundException e) {
        return new FileNotFoundException(e.getMessage(), e.getCause());
    }
}
