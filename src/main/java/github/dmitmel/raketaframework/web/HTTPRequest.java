package github.dmitmel.raketaframework.web;

import java.util.*;

public class HTTPRequest extends HTTPMessage {
    public static final String LINE_SEPARATOR = "\r\n";
    public static final String EMPTY_BODY = "";
    public static final Map<String, String> EMPTY_HEADERS = Collections.emptyMap();

    public final String method;
    public final URL url;
    public final String protocolVersion;
    public final Map<String, String> headers;
    public final String body;

    public HTTPRequest(String method, URL url, String protocolVersion) {
        this(method, url, protocolVersion, EMPTY_HEADERS);
    }

    public HTTPRequest(String method, URL url, String protocolVersion, Map<String, String> headers) {
        this(method, url, protocolVersion, headers, EMPTY_BODY);
    }

    public HTTPRequest(String method, URL url, String protocolVersion, String body) {
        this(method, url, protocolVersion, EMPTY_HEADERS, body);
    }

    public HTTPRequest(String method, URL url, String protocolVersion, Map<String, String> headers, String body) {
        super(protocolVersion, headers, body);

        if (method.trim().isEmpty())
            throw new IllegalArgumentException(method);

        this.method = method;
        this.url = Objects.requireNonNull(url);;
        this.protocolVersion = protocolVersion;
        this.headers = headers;
        this.body = body;
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
        String protocolVersion = mainLineParts[2];

        List<String> headersAndBodyLines = lines.subList(1, lines.size());
        Map<String, String> headers = new HashMap<>(0);
        StringBuilder body = new StringBuilder(0);

        HTTPMessage.parseHeadersAndBodyFromLines(headersAndBodyLines, body, headers);

        return new HTTPRequest(method, url, protocolVersion, headers, body.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(0);

        builder.append(method).append(' ').append(url.toString()).append(' ').append(protocolVersion)
                .append(LINE_SEPARATOR);
        for (Map.Entry<String, String> entry : headers.entrySet())
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR).append(body);

        return builder.toString();
    }
}
