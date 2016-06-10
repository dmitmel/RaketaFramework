package github.dmitmel.raketaframework.pinelog;

public interface Formatter {
    String format(LogMessage logMessage);
    String formatException(ExceptionLogMessage exceptionLogMessage);
}
