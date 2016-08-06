package org.willthisfly.raketaframework.exceptions;

/**
 * {@link java.net.SocketException}
 */
public class SocketException extends IOException {
    public SocketException() {
    }

    public SocketException(String message) {
        super(message);
    }

    public SocketException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketException(Throwable cause) {
        super(cause);
    }

    public static SocketException extractFrom(java.net.SocketException e) {
        return new SocketException(e.getMessage(), e.getCause());
    }
}
