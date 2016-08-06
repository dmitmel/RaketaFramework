package org.willthisfly.raketaframework;

import org.willthisfly.raketaframework.util.ExtendedComparator;
import org.willthisfly.raketaframework.util.Strings;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class HTTPRequest extends HTTPMessage implements Comparable<HTTPRequest> {
    public final String method;
    public final URI uri;

    public HTTPRequest(String method, URI uri, String protocol) {
        this(method, uri, protocol, EMPTY_HEADERS);
    }

    public HTTPRequest(String method, URI uri, String protocol, Map<String, String> headers) {
        this(method, uri, protocol, headers, EMPTY_BODY);
    }

    public HTTPRequest(String method, URI uri, String protocol, byte[] body) {
        this(method, uri, protocol, EMPTY_HEADERS, body);
    }

    public HTTPRequest(String method, URI uri, String protocol, Map<String, String> headers, byte[] body) {
        super(protocol, headers, body);

        if (method.trim().isEmpty())
            throw new IllegalArgumentException(method);

        this.method = method;
        this.uri = Objects.requireNonNull(uri);
    }
    
    
    public static HTTPRequest fromString(String request) {
        return fromBytes(request.getBytes());
    }
    
    public static HTTPRequest fromBytes(byte[] requestBytes) {
        return HTTPRequestParser.parseRequest(requestBytes);
    }
    
    
    @Override
    public byte[] getBytes() {
        return super.toByteArrayWithMainLine(String.format("%s %s %s", method, uri, super.protocol).getBytes());
    }
    
    @Override
    public int length() {
        final int CRLF_LENGTH = Strings.CRLF.length();
        
        int mainRequestLineLength = method.length() + 1 + uri.length() + 1 + protocol.length();
        int headersLength = headers.entrySet().stream()
                .mapToInt(header -> header.getKey().length() + 2 + header.getValue().length() +
                        CRLF_LENGTH)
                .sum();
        int bodyLength = body.length;
        
        return mainRequestLineLength + CRLF_LENGTH +
                headersLength + CRLF_LENGTH +
                bodyLength;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HTTPRequest))
            return false;
        
        HTTPRequest that = (HTTPRequest) o;
        return Objects.equals(this.method, that.method) &&
                Objects.equals(this.uri, that.uri) &&
                Objects.equals(this.protocol, that.protocol) &&
                Objects.equals(this.headers, that.headers) &&
                Arrays.equals(this.body, that.body);
    }
    
    @Override
    public int hashCode() {
        int hashCode = Objects.hash(method, uri, protocol, headers);
        hashCode = 31 * hashCode + (body == null ? 0 : Arrays.hashCode(body));
        return hashCode;
    }
    
    @Override
    public int compareTo(HTTPRequest that) {
        return ExtendedComparator.compareComparingResults(
                ExtendedComparator.compareNullable(this.method, that.method),
                ExtendedComparator.compareNullable(this.uri, that.uri),
                ExtendedComparator.compareNullable(this.protocol, that.protocol),
                ExtendedComparator.compare(this.headers, that.headers),
                ExtendedComparator.compare(this.body, that.body)
        );
    }
}
