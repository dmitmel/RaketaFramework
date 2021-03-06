package org.willthisfly.raketaframework;

import org.willthisfly.raketaframework.util.ExtendedComparator;
import org.willthisfly.raketaframework.util.Numbers;
import org.willthisfly.raketaframework.util.Strings;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class HTTPResponse extends HTTPMessage implements Comparable<HTTPResponse> {
    public final int statusCode;
    public final String statusDescription;

    public HTTPResponse(String protocol, int statusCode, String statusDescription) {
        this(protocol, statusCode, statusDescription, EMPTY_HEADERS);
    }

    public HTTPResponse(String protocol, int statusCode, String statusDescription,
                        Map<String, String> headers) {
        this(protocol, statusCode, statusDescription, headers, EMPTY_BODY);
    }

    public HTTPResponse(String protocol, int statusCode, String statusDescription, byte[] body) {
        this(protocol, statusCode, statusDescription, EMPTY_HEADERS, body);
    }

    public HTTPResponse(String protocol, int statusCode, String statusDescription, Map<String, String> headers,
                        byte[] body) {
        super(protocol, headers, body);

        if (protocol.trim().isEmpty())
            throw new IllegalArgumentException(protocol);

        this.statusCode = statusCode;
        this.statusDescription = Objects.requireNonNull(statusDescription);
    }
    
    
    public static HTTPResponse fromString(String response) {
        return fromBytes(response.getBytes());
    }
    
    public static HTTPResponse fromBytes(byte[] responseBytes) {
        return HTTPResponseParser.parseResponse(responseBytes);
    }
    
    
    @Override
    public int length() {
        final int CRLF_LENGTH = Strings.CRLF.length();
        
        int mainRequestLineLength = protocol.length() + 1 + Numbers.length(statusCode) + 1 + statusDescription.length();
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
        if (!(o instanceof HTTPResponse))
            return false;
    
        HTTPResponse that = (HTTPResponse) o;
        return Objects.equals(this.protocol, that.protocol) &&
                Objects.equals(this.statusCode, that.statusCode) &&
                Objects.equals(this.statusDescription, that.statusDescription) &&
                Objects.equals(this.headers, that.headers) &&
                Arrays.equals(this.body, that.body);
    }
    
    @Override
    public int hashCode() {
        int hashCode = Objects.hash(protocol, statusCode, statusDescription, headers);
        hashCode = 31 * hashCode + (body == null ? 0 : Arrays.hashCode(body));
        return hashCode;
    }
    
    @Override
    public byte[] getBytes() {
        return super.toByteArrayWithMainRequestLine(
                String.format("%s %s %s", super.protocol, statusCode, statusDescription).getBytes());
    }
    
    @Override
    public int compareTo(HTTPResponse that) {
        return ExtendedComparator.compareComparingResults(
                ExtendedComparator.compareNullable(this.protocol, that.protocol),
                Integer.compare(this.statusCode, that.statusCode),
                ExtendedComparator.compareNullable(this.statusDescription, that.statusDescription),
                ExtendedComparator.compare(this.headers, that.headers),
                ExtendedComparator.compare(this.body, that.body)
        );
    }
}
