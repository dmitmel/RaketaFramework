package github.dmitmel.raketaframework.web;

import java.io.ByteArrayOutputStream;
import java.util.*;

public class HTTPRequest extends HTTPMessage {
    public final String method;
    public final URL url;

    public HTTPRequest(String method, URL url, String protocol) {
        this(method, url, protocol, EMPTY_HEADERS);
    }

    public HTTPRequest(String method, URL url, String protocol, Map<String, String> headers) {
        this(method, url, protocol, headers, EMPTY_BODY);
    }

    public HTTPRequest(String method, URL url, String protocol, byte[] body) {
        this(method, url, protocol, EMPTY_HEADERS, body);
    }

    public HTTPRequest(String method, URL url, String protocol, Map<String, String> headers, byte[] body) {
        super(protocol, headers, body);

        if (method.trim().isEmpty())
            throw new IllegalArgumentException(method);

        this.method = method;
        this.url = Objects.requireNonNull(url);
    }


    /**
     * Parses HTTPMessage from request string. Here's example structure of HTTP request:
     * <pre><code>
     * METHOD /a/b/c.d#article?arg=val&other=something HTTP/1.1
     * Header1: Value1
     * Header2: Value2
     *
     * Message's body
     * </code></pre>
     *
     * @param request request string
     * @return parsed HTTPMessage
     */
    public static HTTPRequest fromRequestString(String request) {
        List<String> lines = Arrays.asList(request.split(LINE_SEPARATOR));

        String mainRequestLine = lines.get(0);
        String[] mainLineParts = mainRequestLine.split(" ");

        String method = mainLineParts[0];
        URL url = URL.fromString(mainLineParts[1]);
        String protocol = mainLineParts[2];

        List<String> headersAndBodyLines = lines.subList(1, lines.size());
        Map<String, String> headers = new HashMap<>(0);
        ByteArrayOutputStream body = new ByteArrayOutputStream();

        HTTPMessage.parseHeadersAndBodyFromLines(headersAndBodyLines, body, headers);

        return new HTTPRequest(method, url, protocol, headers, body.toByteArray());
    }

    @Override
    public String toString() {
        return new String(getBytes());
    }

    public byte[] getBytes() {
        return toByteArrayWithMainLine(String.format("%s %s %s", method, url, protocol));
    }
}
