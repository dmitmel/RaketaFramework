package github.dmitmel.raketaframework.util;

/**
 * Exception can be thrown if protocol/class/variable/... isn't annotated with some annotation.
 */
public class AnnotationNotFoundException extends RuntimeException {
    public AnnotationNotFoundException(String message) {
        super(message);
    }
}
