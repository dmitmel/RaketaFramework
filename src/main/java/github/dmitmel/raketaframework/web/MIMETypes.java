package github.dmitmel.raketaframework.web;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class MIMETypes {
    private static final List<String> MIME_DETECTOR_CLASS_NAMES = Collections.unmodifiableList(Arrays.asList(
            "eu.medsea.mimeutil.detector.MagicMimeMimeDetector",
            "eu.medsea.mimeutil.detector.ExtensionMimeDetector"
    ));

    static {
        MIME_DETECTOR_CLASS_NAMES.forEach(MimeUtil::registerMimeDetector);
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                MIME_DETECTOR_CLASS_NAMES.forEach(MimeUtil::unregisterMimeDetector)));
    }

    public static final String PLAIN_TEXT = "text/plain";
    public static final String HTML_DOCUMENT = "text/html";
    public static final String PNG_IMAGE = "image/png";
    public static final String JPEG_IMAGE = "image/jpeg";
    public static final String BMP_IMAGE = "image/x-xbitmap";
    public static final String FAVICON = "image/x-icon";
    public static final String JSON_DOCUMENT = "application/json";
    public static final String CSS_STYLESHEET = "text/css";
    public static final String BYTE_STREAM = "application/octet-stream";

    public static String getContentType(String path) {
        return getContentType0(() -> MimeUtil.getMimeTypes(path));
    }

    public static String getContentType(byte[] content) {
        return getContentType0(() -> MimeUtil.getMimeTypes(content));
    }

    private static String getContentType0(Supplier<Collection> suitableMimeTypesSupplier) {
        MimeType m = MimeUtil.getMostSpecificMimeType(suitableMimeTypesSupplier.get());
        return m.toString();
    }
}
