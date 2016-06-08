package github.dmitmel.raketaframework.web.server;

public class UnAssignableAddressException extends RuntimeException {
    private Exception rawException;
    public Exception getRawException() {
        return rawException;
    }

    public UnAssignableAddressException(String address, Exception rawException) {
        super(address);
        this.rawException = rawException;
    }
}
