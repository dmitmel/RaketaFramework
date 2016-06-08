package github.dmitmel.raketaframework.pinelog;

class ExceptionLogMessage {
    public final String loggerName;
    public final Throwable e;

    public ExceptionLogMessage(String loggerName, Throwable e) {
        this.loggerName = loggerName;
        this.e = e;
    }
}
