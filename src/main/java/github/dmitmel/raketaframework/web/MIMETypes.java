package github.dmitmel.raketaframework.web;

import java.io.ByteArrayInputStream;
import java.net.URLConnection;

public class MIMETypes {
    public static final String PLAIN_TEXT = "text/plain";
    public static final String HTML_DOCUMENT = "text/html";
    public static final String PNG_IMAGE = "image/png";
    public static final String JPEG_IMAGE = "image/jpeg";
    public static final String BMP_IMAGE = "image/x-xbitmap";
    public static final String FAVICON = "image/x-icon";
    public static final String JSON_DOCUMENT = "application/json";
    public static final String CSS_STYLESHEET = "text/css";
    public static final String BYTE_STREAM = "application/octet-stream";

    public static String getContentTypeOrDefault(byte[] content) {
        return getContentTypeOrDefault(content, BYTE_STREAM);
    }

    public static String getContentTypeOrDefault(byte[] content, String defaultContentType) {
        String mime = getContentType(content);
        return mime == null ? defaultContentType : mime;
    }

    public static String getContentType(byte[] content) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
            return URLConnection.guessContentTypeFromStream(inputStream);
        } catch (java.io.IOException e) {
            throw github.dmitmel.raketaframework.util.exceptions.IOException.extractFrom(e);
        }
    }
}
