package github.dmitmel.raketaframework.server;

public class PortIsListeningException extends RuntimeException {
    public final int port;
    
    public PortIsListeningException(int port) {
        super(formatMessageWithPort(port));
        this.port = port;
    }
    
    public PortIsListeningException(int port, Throwable cause) {
        super(formatMessageWithPort(port), cause);
        this.port = port;
    }
    
    private static String formatMessageWithPort(int port) {
        return String.format("Someone is listening on the port %d", port);
    }
}
