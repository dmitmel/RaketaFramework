package github.dmitmel.raketaframework.web;

import github.dmitmel.raketaframework.util.Pair;
import github.dmitmel.raketaframework.util.StringUtils;

import java.util.*;

public abstract class HTTPMessage {
    public static final String EMPTY_BODY = StringUtils.EMPTY_STRING;
    public static final Map<String, String> EMPTY_HEADERS = Collections.emptyMap();
    public static final String LINE_SEPARATOR = "\r\n";

    public final String protocol;
    public final Map<String, String> headers;
    public final String body;

    public HTTPMessage(String protocol, Map<String, String> headers, String body) {
        if (protocol.trim().isEmpty())
            throw new IllegalArgumentException(protocol);

        this.protocol = protocol;
        this.headers = Objects.requireNonNull(headers);;
        this.body = Objects.requireNonNull(body);;
    }

    public static void parseHeadersAndBodyFromLines(List<String> headersAndBodyLines, StringBuilder body,
                                                    Map<String, String> headers) {
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
    }

    private static Pair<String, String> parseHeaderFromLine(String line) {
        String[] parts = line.split(": ");
        return new Pair<>(parts[0], parts[1]);
    }

    public abstract String toString();
}
