package github.dmitmel.raketaframework.pinelog;

import java.io.PrintStream;

public class ConsoleAppender implements Appender {
    public PrintStream printStream;
    public Formatter formatter;


    public ConsoleAppender() {
        this(System.err);
    }

    public ConsoleAppender(PrintStream printStream) {
        this(new SimpleFormatter(), printStream);
    }

    public ConsoleAppender(Formatter formatter) {
        this(formatter, System.err);
    }

    public ConsoleAppender(Formatter formatter, PrintStream printStream) {
        if (printStream == null)
            printStream = System.err;
        if (formatter == null)
            formatter = new SimpleFormatter();
        this.formatter = formatter;
        this.printStream = printStream;
    }


    @Override
    public void appendMessage(LogMessage logMessage) {
        printStream.println(formatter.format(logMessage));
    }

    @Override
    public void appendExceptionMessage(ExceptionLogMessage logMessage) {
        printStream.println(formatter.formatException(logMessage));
    }
}
