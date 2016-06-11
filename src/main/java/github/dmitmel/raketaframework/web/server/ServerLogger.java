package github.dmitmel.raketaframework.web.server;

import java.util.function.Predicate;

class ServerLogger {
    public static final Predicate<LoggingLevel> ALL_LEVELS_FILTERER = (loggingLevel -> true);

    public Predicate<LoggingLevel> logLevelFilterer = ALL_LEVELS_FILTERER;
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
