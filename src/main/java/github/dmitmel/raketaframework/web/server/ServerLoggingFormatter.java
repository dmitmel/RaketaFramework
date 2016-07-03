package github.dmitmel.raketaframework.web.server;

import github.dmitmel.raketaframework.web.HTTPDateFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;

class ServerLoggingFormatter {
    public static final String MESSAGE_FORMAT = "[%s]: %-23s %s";

    public String format(LoggingLevel level, String message) {
        return String.format(MESSAGE_FORMAT, HTTPDateFormatter.formatCurrentDate(), level.toString() + ':', message);
    }

    public String formatException(Throwable e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));

        return format(LoggingLevel.EXCEPTION, stackTrace.toString());
    }
}
