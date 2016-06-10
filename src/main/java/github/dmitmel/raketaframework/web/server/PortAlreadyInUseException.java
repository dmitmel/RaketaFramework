package github.dmitmel.raketaframework.web.server;

public class PortAlreadyInUseException extends RuntimeException {
    private Exception rawException;
    public Exception getRawException() {
        return rawException;
    }

    public PortAlreadyInUseException(int port, Exception rawException) {
        super(String.format("Port %d is already registered in OS as listening", port));
        this.rawException = rawException;
    }
}
