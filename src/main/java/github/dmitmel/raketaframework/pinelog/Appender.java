package github.dmitmel.raketaframework.pinelog;

public interface Appender {
    void appendMessage(LogMessage logMessage);
    void appendExceptionMessage(ExceptionLogMessage logMessage);
}
