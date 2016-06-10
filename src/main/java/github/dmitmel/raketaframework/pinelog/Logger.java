package github.dmitmel.raketaframework.pinelog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Logger {
    public static final List<Appender> DEFAULT_APPENDER_LIST =
            Collections.unmodifiableList(Collections.singletonList(new ConsoleAppender()));

    public List<Appender> appenderList = new ArrayList<>(0);
    public String name;


    public Logger(String name) {
        this(name, DEFAULT_APPENDER_LIST);
    }

    public Logger(String name, List<Appender> appenderList) {
        this.name = name;
        this.appenderList = appenderList;
    }


    public Logger log(Level level, String message) {
        for (Appender appender : appenderList)
            appender.appendMessage(new LogMessage(this.name, level, message));
        return this;
    }

    public Logger info(String message) { return log(Level.INFO, message); }
    public Logger debug(String message) { return log(Level.DEBUG, message); }
    public Logger warning(String message) { return log(Level.WARNING, message); }
    public Logger error(String message) { return log(Level.ERROR, message); }
    public Logger fatal(String message) { return log(Level.FATAL, message); }

    public Logger exception(Throwable e) {
        for (Appender appender : appenderList)
            appender.appendExceptionMessage(new ExceptionLogMessage(this.name, e));
        return this;
    }
}
