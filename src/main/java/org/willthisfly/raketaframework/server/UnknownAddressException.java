package org.willthisfly.raketaframework.server;

public class UnknownAddressException extends RuntimeException {
    public final String address;
    
    public UnknownAddressException(String address) {
        super(address);
        this.address = address;
    }
    
    public UnknownAddressException(String address, Throwable cause) {
        super(address, cause);
        this.address = address;
    }
}
