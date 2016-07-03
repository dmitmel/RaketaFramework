package github.dmitmel.raketaframework.web;

import github.dmitmel.raketaframework.util.StringUtils;
import github.dmitmel.raketaframework.util.Terminal;

import java.io.*;

public class MIMETypes {
    public static final String PLAIN_TEXT = "text/plain";
    public static final String HTML_DOCUMENT = "text/html";
    public static final String PNG_IMAGE = "image/png";
    public static final String JPEG_IMAGE = "image/jpeg";
    public static final String BMP_IMAGE = "image/x-xbitmap";
    public static final String FAVICON = "image/x-icon";
    public static final String JSON_DOCUMENT = "application/json";
    public static final String CSS_STYLESHEET = "text/css";

    public static String getContentType(String name) {
        try {
            if (new File(name).exists()) {
                Process process = Terminal.execAndWaitUntilStops("file --mime-type " + name);

                if (process != Terminal.INTERRUPTED_PROCESS_INSTANCE) {
                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    StringBuilder builder = new StringBuilder(0);
                    for (String line = StringUtils.EMPTY_STRING; line != null; line = inputStream.readLine())
                        builder.append(line).append('\n');

                    // Command "file --mime-type" returns such string:
                    // /path/to/file: mime/type
                    return builder.toString().split(":")[1].trim();
                } else {
                    throw new github.dmitmel.raketaframework.util.exceptions.IOException("can\'t determine file type");
                }
            } else {
                throw new github.dmitmel.raketaframework.util.exceptions.FileNotFoundException(name);
            }
        } catch (java.io.IOException e) {
            throw github.dmitmel.raketaframework.util.exceptions.IOException.extractFrom(e);
        }
    }
}
