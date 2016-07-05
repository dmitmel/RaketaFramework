package github.dmitmel.raketaframework.web;

import github.dmitmel.raketaframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public abstract class HTTPMessage {
    public static final byte[] EMPTY_BODY = StringUtils.EMPTY_STRING.getBytes();
    public static final Map<String, String> EMPTY_HEADERS = Collections.emptyMap();
    public static final String LINE_SEPARATOR = "\r\n";

    public final String protocol;
    public final Map<String, String> headers;
    public final byte[] body;

    public HTTPMessage(String protocol, Map<String, String> headers, byte[] body) {
        if (protocol.trim().isEmpty())
            throw new IllegalArgumentException(protocol);

        this.protocol = protocol;
        this.headers = Objects.requireNonNull(headers);
        this.body = body;
    }

    public static void parseHeadersAndBodyFromLines(List<String> headersAndBodyLines, ByteArrayOutputStream body,
                                                    Map<String, String> headers) {
        try {
            boolean readingHeaders = true;

            for (Iterator<String> iterator = headersAndBodyLines.iterator(); iterator.hasNext(); ) {
                String line = iterator.next();

                if (line.isEmpty()) {
                    readingHeaders = false;
                } else {
                    if (readingHeaders) {
                        Map<String, String> header = parseHeaderFromLine(line);
                        headers.putAll(header);
                    } else {
                        body.write(line.getBytes());
                        if (iterator.hasNext())
                            body.write(LINE_SEPARATOR.getBytes());
                    }
                }
            }
        } catch (IOException e) {
            // This code is unreachable, because java.io.ByteArrayOutputStream doesn't throw IOException, but this
            // exception is declared in the method signatures in the java.io.OutputStream
            throw new RuntimeException("unreachable code");
        }
    }

    protected byte[] toByteArrayWithMainLine(String mainLine) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            stream.write((mainLine + LINE_SEPARATOR).getBytes());
            for (Map.Entry<String, String> entry : headers.entrySet())
                stream.write(String.format("%s: %s%s", entry.getKey(), entry.getValue(), LINE_SEPARATOR).getBytes());
            stream.write(LINE_SEPARATOR.getBytes());
            stream.write(body);

            return stream.toByteArray();
        } catch (IOException e) {
            // This code is unreachable, because java.io.ByteArrayOutputStream doesn't throw IOException, but this
            // exception is declared in the method signatures in the java.io.OutputStream
            throw new RuntimeException("unreachable code");
        }
    }

    private static Map<String, String> parseHeaderFromLine(String line) {
        String[] parts = line.split(": ");
        return Collections.singletonMap(parts[0], parts[1]);
    }

    public abstract String toString();
}
