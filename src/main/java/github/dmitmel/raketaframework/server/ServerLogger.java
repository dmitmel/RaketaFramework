package github.dmitmel.raketaframework.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

class ServerLogger {
    private static final List<LoggingLevel> DEFAULT_ACCEPTABLE_LEVELS = Collections.unmodifiableList(Arrays.asList(
            LoggingLevel.INFO, LoggingLevel.SUMMARY, LoggingLevel.EXCEPTION, LoggingLevel.FATAL_ERROR
    ));
    private static final Predicate<LoggingLevel> DEFAULT_FILTERER = (DEFAULT_ACCEPTABLE_LEVELS::contains);

    Predicate<LoggingLevel> logLevelFilterer = DEFAULT_FILTERER;
    private final ServerLoggingFormatter formatter = new ServerLoggingFormatter();

    void log(LoggingLevel level, String message, Object... args) {
        if (logLevelFilterer.test(level)) {
            String realMessage = String.format(message, args);
            System.err.println(formatter.format(level, realMessage));
        }
    }

    void exception(Throwable e) {
        if (logLevelFilterer.test(LoggingLevel.EXCEPTION))
            System.err.print(formatter.formatException(e));
    }
}
