package github.dmitmel.raketaframework.web;

import github.dmitmel.raketaframework.util.Pair;
import github.dmitmel.raketaframework.util.StringUtils;

import java.util.*;

public class HTTPResponse {
    public static final String LINE_SEPARATOR = "\r\n";
    public static final String EMPTY_BODY = StringUtils.EMPTY_STRING;
    public static final Map<String, String> EMPTY_HEADERS = Collections.emptyMap();

    public final String protocolVersion;
    public final int statusCode;
    public final String statusDescription;
    public final Map<String, String> headers;
    public final String body;

    public HTTPResponse(String protocolVersion, int statusCode, String statusDescription) {
        this(protocolVersion, statusCode, statusDescription, EMPTY_HEADERS);
    }

    public HTTPResponse(String protocolVersion, int statusCode, String statusDescription,
                        Map<String, String> headers) {
        this(protocolVersion, statusCode, statusDescription, headers, EMPTY_BODY);
    }

    public HTTPResponse(String protocolVersion, int statusCode, String statusDescription, String body) {
        this(protocolVersion, statusCode, statusDescription, EMPTY_HEADERS, body);
    }

    public HTTPResponse(String protocolVersion, int statusCode, String statusDescription, Map<String, String> headers,
                        String body) {
        if (protocolVersion.trim().isEmpty())
            throw new IllegalArgumentException(protocolVersion);
        Objects.requireNonNull(statusDescription);
        Objects.requireNonNull(headers);

        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
        this.headers = headers;
        this.body = body;
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

        String protocolVersion = statusLineParts[0];
        int statusCode = Integer.parseInt(statusLineParts[1]);
        String statusDescription = statusLineParts[2];

        List<String> headersAndBodyLines = lines.subList(1, lines.size());
        Map<String, String> headers = new HashMap<>(0);
        StringBuilder body = new StringBuilder(0);

        boolean readingHeaders = true;
        for (Iterator<String> iterator = headersAndBodyLines.iterator(); iterator.hasNext(); ) {
            String line = iterator.next();

            if (line.isEmpty()) {
                readingHeaders = false;
            } else {
                if (readingHeaders) {
                    Pair<String, String> header = parseHeaderFromLine(line);
                    headers.putAll(header.asOneElementMap());
                } else {
                    body.append(line);
                    if (iterator.hasNext())
                        body.append(LINE_SEPARATOR);
                }
            }
        }

        return new HTTPResponse(protocolVersion, statusCode, statusDescription, headers, body.toString());
    }

    private static Pair<String, String> parseHeaderFromLine(String line) {
        String[] parts = line.split(": ");
        return new Pair<>(parts[0], parts[1]);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(0);

        builder.append(protocolVersion).append(' ').append(statusCode).append(' ').append(statusDescription)
                .append(LINE_SEPARATOR);
        for (Map.Entry<String, String> entry : headers.entrySet())
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR).append(body);

        return builder.toString();
    }
}
