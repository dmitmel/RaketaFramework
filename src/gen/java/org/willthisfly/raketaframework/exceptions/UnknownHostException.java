package org.willthisfly.raketaframework.exceptions;

/**
 * {@link java.net.UnknownHostException}
 */
public class UnknownHostException extends SocketException {
    public UnknownHostException() {
    }

    public UnknownHostException(String message) {
        super(message);
    }

    public UnknownHostException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownHostException(Throwable cause) {
        super(cause);
    }

    public static UnknownHostException extractFrom(java.net.UnknownHostException e) {
        return new UnknownHostException(e.getMessage(), e.getCause());
    }
}
