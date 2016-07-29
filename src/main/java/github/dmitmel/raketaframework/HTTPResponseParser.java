package github.dmitmel.raketaframework;

import github.dmitmel.raketaframework.util.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPResponseParser {
    public static final byte[] EMPTY_BODY = Strings.EMPTY_STRING.getBytes();
    public static final Map<String, String> EMPTY_HEADERS = Collections.emptyMap();
    
    private static final ByteString CRLF_BYTE_STRING = new ByteString(Strings.CRLF);
    private static final ByteString SPACE_BYTE_STRING = new ByteString(" ");
    private static final ByteString DOUBLE_CRLF_BYTE_STRING = CRLF_BYTE_STRING.concat(CRLF_BYTE_STRING);
    
    
    private HTTPResponseParser() {
        throw new RuntimeException("Can\'t create instance of HTTPResponseParser");
    }
    
    
    private static final Pattern HEADER_PARSE_PATTERN = Pattern.compile(
            "(?<name>[^:]+)" +
            ":\\s*" +
            "(?<value>.+)");
    
    
    /**
     * Parses response from bytes. Here's example structure of
     * HTTP response:
     *
     * <pre><code>
     * HTTP/1.1 200 OK
     * Header1: Value1
     * Header2: Value2
     *
     * Message's body
     * </code></pre>
     */
    public static HTTPResponse parseResponse(byte[] responseBytes) {
        ByteString responseString = new ByteString(responseBytes);
        
        ByteString mainResponseLine = takeBytesBeforeOptional(CRLF_BYTE_STRING, 0, responseString);
        
        String protocol = takeBytesBeforeOptional(SPACE_BYTE_STRING, 0, mainResponseLine).toString();
        if (protocol.isEmpty())
            throw new SyntaxException("Missing protocol", mainResponseLine, 0);
    
        // 'protocol.length()' - make offset point to spacer after protocol
        // '+ SPACE_BYTE_STRING.length()' - skip spacer
        int statusCodeOffset = protocol.length() + SPACE_BYTE_STRING.length();
        ByteString statusCodeString = takeBytesBeforeOptional(SPACE_BYTE_STRING, statusCodeOffset, mainResponseLine);
        if (statusCodeString.isEmpty())
            throw new SyntaxException("Missing status code", mainResponseLine, statusCodeOffset - SPACE_BYTE_STRING.length());
        int statusCode = Integer.valueOf(statusCodeString.toString());
        
        // 'statusCodeOffset + statusCodeString.length()' - make offset point to spacer after status code
        // '+ SPACE_BYTE_STRING.length()' - skip spacer
        int statusDescriptionOffset = statusCodeOffset + statusCodeString.length() + SPACE_BYTE_STRING.length();
        String statusDescription = takeBytesBeforeOptional(SPACE_BYTE_STRING, statusDescriptionOffset, mainResponseLine).toString();
        if (statusDescription.isEmpty())
            throw new SyntaxException("Missing status description", mainResponseLine, statusDescriptionOffset - SPACE_BYTE_STRING.length());
        
        // 'mainResponseLine.length()' - make offset point to the start of CRLF after main response line
        int headersOffset = mainResponseLine.length();
        // I'm using 'takeBytesBefore' instead of 'takeBytesBeforeOptional', to get empty headers lines if there
        // aren't any of them
        ByteString headersLines = takeBytesBefore(DOUBLE_CRLF_BYTE_STRING, headersOffset, responseString);
        Map<String, String> headers = parseHeadersFromLines(headersLines);
    
        // 'mainResponseLine.length()' - make offset point to the start of the double CRLF after headers
        // '+ CRLF_BYTE_STRING.length()' - move offset the to the end of the double CRLF after main request line
        int bodyOffset = headersOffset + headersLines.length() + DOUBLE_CRLF_BYTE_STRING.length();
        byte[] body = tryToMakeOffset(responseString, bodyOffset).bytes;
        if (body.length == 0)
            body = EMPTY_BODY;
        
        return new HTTPResponse(protocol, statusCode, statusDescription, headers, body);
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
