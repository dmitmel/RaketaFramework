package github.dmitmel.raketaframework.web.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

class ServerLoggingFormatter {
    public static final String MESSAGE_FORMAT = "[%s]: %-10s %s";
    public static final String DATE_FORMAT = "dd/MMM/yyyy hh:mm:ss";

    public String format(LoggingLevel level, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String date = dateFormat.format(new Date());

        return String.format(MESSAGE_FORMAT, date, level.toString() + ':', message);
    }

    public String formatException(Throwable e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));

        return format(LoggingLevel.EXCEPTION, stackTrace.toString());
    }
}
