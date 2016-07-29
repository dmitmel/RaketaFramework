package github.dmitmel.raketaframework;

import java.io.ByteArrayInputStream;
import java.net.URLConnection;

public class MIMETypes {
    public static final String PLAIN_TEXT = "text/plain";
    public static final String BYTE_STREAM = "application/octet-stream";
    public static final String FORM = "application/x-www-form-urlencoded";
    
    
    private MIMETypes() {
        throw new RuntimeException("Can\'t create instance of MIMETypes");
    }
    

    public static String getContentTypeOrElse(byte[] content) {
        return getContentTypeOrElse(content, BYTE_STREAM);
    }

    public static String getContentTypeOrElse(byte[] content, String orElse) {
        String mime = getContentType(content);
        return mime == null ? orElse : mime;
    }

    public static String getContentType(byte[] content) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
            return URLConnection.guessContentTypeFromStream(inputStream);
        } catch (java.io.IOException e) {
            throw github.dmitmel.raketaframework.exceptions.IOException.extractFrom(e);
        }
    }
}
