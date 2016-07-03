package github.dmitmel.raketaframework.web.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

class ServerLogger {
    public static final List<LoggingLevel> DEFAULT_ACCEPTABLE_LEVELS = Collections.unmodifiableList(Arrays.asList(
            LoggingLevel.INFO, LoggingLevel.REQUEST_SUMMARY, LoggingLevel.EXCEPTION, LoggingLevel.FATAL_ERROR
    ));
    public static final Predicate<LoggingLevel> DEFAULT_FILTERER = (DEFAULT_ACCEPTABLE_LEVELS::contains);

    public Predicate<LoggingLevel> logLevelFilterer = DEFAULT_FILTERER;
    private ServerLoggingFormatter formatter = new ServerLoggingFormatter();

    public void log(LoggingLevel level, String message) {
        if (logLevelFilterer.test(level))
            System.err.println(formatter.format(level, message));
    }

    public void exception(Throwable e) {
        if (logLevelFilterer.test(LoggingLevel.EXCEPTION))
            System.err.println(formatter.formatException(e));
    }
}
