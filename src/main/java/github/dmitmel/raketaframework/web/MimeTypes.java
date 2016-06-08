package github.dmitmel.raketaframework.web;

public class MimeTypes {
    public static final String PLAIN_TEXT = "text/plain";
    public static final String HTML_DOCUMENT = "text/html";
    public static final String PNG_IMAGE = "image/png";
    public static final String JPEG_IMAGE = "image/jpeg";
    public static final String BMP_IMAGE = "image/x-xbitmap";
    public static final String FAVICON = "image/x-icon";
    public static final String JSON_DOCUMENT = "application/json";
    public static final String CSS_STYLESHEET = "text/css";
    
    public static String getApproximateTypeFor(String filePath) {
        String mime;
        
        if (filePath.endsWith("htm") || filePath.endsWith("html")) {
            mime = HTML_DOCUMENT;
        } else if (filePath.endsWith("png")) {
            mime = PNG_IMAGE;
        } else if (filePath.endsWith("jpg") || filePath.endsWith("jpeg")) {
            mime = JPEG_IMAGE;
        } else if (filePath.endsWith("bmp")) {
            mime = BMP_IMAGE;
        } else if (filePath.endsWith("json")) {
            mime = JSON_DOCUMENT;
        } else if (filePath.endsWith("css")) {
            mime = CSS_STYLESHEET;
        } else if (filePath.endsWith("ico")) {
            mime = FAVICON;
        } else {
            mime = PLAIN_TEXT;
        }

        return mime;
    }
}
