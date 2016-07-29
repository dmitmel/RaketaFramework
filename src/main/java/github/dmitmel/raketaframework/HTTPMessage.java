package github.dmitmel.raketaframework;

import github.dmitmel.raketaframework.util.*;

import java.io.*;
import java.util.*;

public abstract class HTTPMessage implements Cloneable {
    public static final byte[] EMPTY_BODY = Strings.EMPTY_STRING.getBytes();
    public static final Map<String, String> EMPTY_HEADERS = Collections.emptyMap();
    
    public final String protocol;
    public final Map<String, String> headers;
    public final byte[] body;

    public HTTPMessage(String protocol, Map<String, String> headers, byte[] body) {
        if (protocol.trim().isEmpty())
            throw new IllegalArgumentException(protocol);

        this.protocol = protocol;
        this.headers = Objects.requireNonNull(headers);
        this.body = Objects.requireNonNull(body);
    }
    
    public abstract byte[] getBytes();
    @Override
    public abstract int hashCode();
    @Override
    public abstract boolean equals(Object o);
    
    protected byte[] toByteArrayWithMainLine(byte[] mainLine) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            stream.write(mainLine);
            stream.write(Strings.CRLF.getBytes());
            
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String line = String.format("%s: %s%s", entry.getKey(), entry.getValue(), Strings.CRLF);
                stream.write(line.getBytes());
            }
            
            stream.write(Strings.CRLF.getBytes());
            stream.write(body);

            return stream.toByteArray();
        } catch (IOException e) {
            // This code is unreachable, because java.io.ByteArrayOutputStream doesn't throw IOException, but this
            // exception is declared in the method signatures in the java.io.OutputStream
            throw new RuntimeException("unreachable code");
        }
    }
    
    public String bodyToString() {
        return new String(this.body);
    }
    
    @Override
    public String toString() {
        return new String(getBytes());
    }
    
    @Override
    public HTTPMessage clone() {
        try {
            return (HTTPMessage) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("unreachable code");
        }
    }
}
