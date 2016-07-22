package github.dmitmel.raketaframework.web;

import java.io.ByteArrayOutputStream;
import java.util.*;

public class HTTPResponse extends HTTPMessage {
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


    /**
     * Parses HTTPResponse from request string. Here's example structure of HTTP response:
     * <pre><code>
     * HTTP/1.1 200 OK
     * Header1: Value1
     * Header2: Value2
     *
     * Message's body
     * </code></pre>
     *
     * @param request request string
     * @return parsed HTTPMessage
     */
    public static HTTPResponse fromRequestString(String request) {
        List<String> lines = Arrays.asList(request.split(LINE_SEPARATOR));
        
        String statusLine = lines.get(0);
        String[] statusLineParts = statusLine.split(" ");
        
        String protocol = statusLineParts[0];
        int statusCode = Integer.parseInt(statusLineParts[1]);
        String statusDescription = statusLineParts[2];
        
        List<String> headersAndBodyLines = lines.subList(1, lines.size());
        Map<String, String> headers = new HashMap<>(0);
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        
        HTTPMessage.parseHeadersAndBodyFromLines(headersAndBodyLines, body, headers);
        
        return new HTTPResponse(protocol, statusCode, statusDescription, headers, body.toByteArray());
    }
    
    public String bodyToString() {
        return new String(super.body);
    }

    @Override
    public String toString() {
        return new String(getBytes());
    }

    public byte[] getBytes() {
        return super.toByteArrayWithMainLine(String.format("%s %s %s", super.protocol, statusCode, statusDescription));
    }
}
