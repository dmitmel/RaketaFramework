package github.dmitmel.raketaframework.pinelog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleFormatter implements Formatter {
    public static final String MESSAGE_FORMAT = "[%s]: %-10s %s";
    public static final String DATE_FORMAT = "dd/MMM/yyyy hh:mm:ss";

    @Override
    public String format(LogMessage logMessage) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String date = dateFormat.format(new Date());

        return String.format(MESSAGE_FORMAT, date, logMessage.level.toString() + ':', logMessage.message);
    }

    @Override
    public String formatException(ExceptionLogMessage exceptionLogMessage) {
        StringWriter stackTrace = new StringWriter();
        exceptionLogMessage.e.printStackTrace(new PrintWriter(stackTrace));

        return format(new LogMessage(exceptionLogMessage.loggerName, Level.EXCEPTION, stackTrace.toString()));
    }
}
