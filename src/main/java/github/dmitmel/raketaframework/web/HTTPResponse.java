package github.dmitmel.raketaframework.web;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPResponse extends HTTPMessage {
    public static final Pattern IMAGE_CONTENT_TYPE_PATTERN =
            Pattern.compile("image/(gif|jpeg|pjpeg|png|svf\\+xml|tiff|vnd\\.microsoft\\.icon|vnd\\.wap\\.wbmp)");

    public final int statusCode;
    public final String statusDescription;

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
        super(protocolVersion, headers, body);

        if (protocolVersion.trim().isEmpty())
            throw new IllegalArgumentException(protocolVersion);

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

        String protocolVersion = statusLineParts[0];
        int statusCode = Integer.parseInt(statusLineParts[1]);
        String statusDescription = statusLineParts[2];

        List<String> headersAndBodyLines = lines.subList(1, lines.size());
        Map<String, String> headers = new HashMap<>(0);
        StringBuilder body = new StringBuilder(0);

        HTTPMessage.parseHeadersAndBodyFromLines(headersAndBodyLines, body, headers);

        return new HTTPResponse(protocolVersion, statusCode, statusDescription, headers, body.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(0);

        builder.append(protocolVersion).append(' ').append(statusCode).append(' ').append(statusDescription)
                .append(LINE_SEPARATOR);
        for (Map.Entry<String, String> entry : headers.entrySet())
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append(LINE_SEPARATOR);
        builder.append(LINE_SEPARATOR);

        if (headers.containsKey("Content-Type")) {
            String contentType = headers.get("Content-Type");
            Matcher contentTypeMatcher = IMAGE_CONTENT_TYPE_PATTERN.matcher(contentType);
            if (contentTypeMatcher.matches()) {
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(body.getBytes()));

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(image, contentTypeMatcher.group(1), byteArrayOutputStream);

                    byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
                    outputStream.write(size);
                    outputStream.write(byteArrayOutputStream.toByteArray());
                    outputStream.flush();
                    builder.append(outputStream);
                } catch (java.io.IOException e) {
                    throw github.dmitmel.raketaframework.util.exceptions.IOException.extractFrom(e);
                }
            } else {
                builder.append(body);
            }
        } else {
            builder.append(body);
        }

        return builder.toString();
    }
}
