package github.dmitmel.raketaframework;

import github.dmitmel.raketaframework.util.*;

import java.util.*;
import java.util.regex.*;

public class HTTPRequestParser {
    public static final byte[] EMPTY_BODY = Strings.EMPTY_STRING.getBytes();
    public static final Map<String, String> EMPTY_HEADERS = Collections.emptyMap();
    
    private static final ByteString CRLF_BYTE_STRING = new ByteString(Strings.CRLF);
    private static final ByteString SPACE_BYTE_STRING = new ByteString(" ");
    private static final ByteString DOUBLE_CRLF_BYTE_STRING = CRLF_BYTE_STRING.concat(CRLF_BYTE_STRING);
    
    private static final Pattern HEADER_PARSE_PATTERN = Pattern.compile(
            "(?<name>[^:]+)" +
            ":\\s*" +
            "(?<value>.+)");
    
    
    private HTTPRequestParser() {
        throw new RuntimeException("Can\'t create instance of HTTPRequestParser");
    }
    
    
    /**
     * Parses request from bytes. Here's example structure of
     * HTTP request:
     *
     * <pre><code>
     * METHOD /a/b/c.d#anchor?arg=val&other=something HTTP/1.1
     * Header1: Value1
     * Header2: Value2
     *
     * Message's body
     * </code></pre>
     */
    public static HTTPRequest parseRequest(byte[] requestBytes) {
        ByteString requestString = new ByteString(requestBytes);
        
        ByteString mainRequestLine = takeBytesBeforeOptional(CRLF_BYTE_STRING, 0, requestString);
        
        String method = takeBytesBeforeOptional(SPACE_BYTE_STRING, 0, mainRequestLine).toString();
        if (method.isEmpty())
            throw new SyntaxException("Missing method", mainRequestLine, 0);
    
        // 'method.length()' - make offset point to spacer after method
        // '+ SPACE_BYTE_STRING.length()' - skip spacer
        int uriOffset = method.length() + SPACE_BYTE_STRING.length();
        ByteString uriString = takeBytesBeforeOptional(SPACE_BYTE_STRING, uriOffset, mainRequestLine);
        if (uriString.isEmpty())
            throw new SyntaxException("Missing URI", mainRequestLine, uriOffset - SPACE_BYTE_STRING.length());
        URI uri = URI.fromString(uriString.toString().trim());
    
        // 'uriOffset + uriString.length()' - make offset point to spacer after URI
        // '+ SPACE_BYTE_STRING.length()' - skip spacer
        int protocolOffset = uriOffset + uriString.length() + SPACE_BYTE_STRING.length();
        String protocol = takeBytesBeforeOptional(SPACE_BYTE_STRING, protocolOffset, mainRequestLine).toString();
        if (protocol.isEmpty())
            throw new SyntaxException("Missing protocol", mainRequestLine, protocolOffset - SPACE_BYTE_STRING.length());
        
        // 'mainRequestLine.length()' - make offset point to the start of CRLF after main request line
        int headersOffset = mainRequestLine.length();
        // I'm using 'takeBytesBefore' instead of 'takeBytesBeforeOptional', to get empty headers lines if there
        // aren't any of them
        ByteString headersLines = takeBytesBefore(DOUBLE_CRLF_BYTE_STRING, headersOffset, requestString);
        Map<String, String> headers = parseHeadersFromLines(headersLines);
    
        // 'mainRequestLine.length()' - make offset point to the start of the double CRLF after headers
        // '+ CRLF_BYTE_STRING.length()' - move offset the to the end of the double CRLF after main request line
        int bodyOffset = headersOffset + headersLines.length() + DOUBLE_CRLF_BYTE_STRING.length();
        byte[] body = tryToMakeOffset(requestString, bodyOffset).bytes;
        if (body.length == 0)
            body = EMPTY_BODY;
        
        return new HTTPRequest(method, uri, protocol, headers, body);
    }
    
    private static Map<String, String> parseHeadersFromLines(ByteString headersLines) {
        HashMap<String, String> headers = new HashMap<>();

        for (ByteString line : headersLines.split(CRLF_BYTE_STRING)) {
            if (!line.isWhitespace()) {
                Map.Entry<String, String> header = parseHeaderFromLine(line);
                String name = header.getKey();
                String value = header.getValue();
                headers.put(name, value);
            }
        }
        
        return headers.isEmpty() ? EMPTY_HEADERS : headers;
    }
    
    private static Map.Entry<String, String> parseHeaderFromLine(ByteString line) {
        Matcher matcher = Patterns.matcherWithCaching(HEADER_PARSE_PATTERN, line.toString());
        
        if (matcher.matches()) {
            String name = matcher.group("name");
            String value = matcher.group("value");
            return new SimpleEntry<>(name, value);
        } else {
            throw new SyntaxException("No header", line, 0);
        }
    }
    
    private static ByteString takeBytesBeforeOptional(ByteString sequence, int offset, ByteString string) {
        ByteString result = takeBytesBefore(sequence, offset, string);
        if (result.isEmpty())
            return tryToMakeOffset(string, offset);
        else
            return result;
    }
    
    private static ByteString takeBytesBefore(ByteString sequence, int offset, ByteString string) {
        ByteString withOffset = tryToMakeOffset(string, offset);
        int startIndex = withOffset.indexOf(sequence);
    
        if (startIndex == -1)
            return ByteString.EMPTY;
        else
            return withOffset.substring(0, startIndex);
    }
    
    private static ByteString tryToMakeOffset(ByteString string, int start) {
        if (start > string.length())
            return ByteString.EMPTY;
        else
            return string.substring(start);
    }
}
