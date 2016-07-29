package github.dmitmel.raketaframework.server;

import github.dmitmel.raketaframework.HTTPDateFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;

class ServerLoggingFormatter {
    private static final String MESSAGE_FORMAT = "[%s]: %-23s %s";

    String format(LoggingLevel level, String message) {
        return String.format(MESSAGE_FORMAT, HTTPDateFormatter.currentDateInServerLogFormat(), level.toString() + ':', message);
    }

    String formatException(Throwable e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));

        return format(LoggingLevel.EXCEPTION, stackTrace.toString());
    }
}
