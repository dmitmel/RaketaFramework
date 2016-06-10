package github.dmitmel.raketaframework.pinelog;

class LogMessage {
    public final String loggerName;
    public final String message;
    public final Level level;

    public LogMessage(String loggerName, Level level, String message) {
        this.loggerName = loggerName;
        this.level = level;
        this.message = message;
    }
}
