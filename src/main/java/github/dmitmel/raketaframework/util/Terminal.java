package github.dmitmel.raketaframework.util;

import java.io.InputStream;
import java.io.OutputStream;

public class Terminal {
    public static Process exec(String command) {
        try {
            return Runtime.getRuntime().exec(command);
        } catch (java.io.IOException e) {
            throw github.dmitmel.raketaframework.util.exceptions.IOException.extractFrom(e);
        }
    }

    public static Process execAndWaitUntilStops(String command) {
        Process process = NULL_PROCESS_INSTANCE;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            return process;
        } catch (java.io.IOException e) {
            throw github.dmitmel.raketaframework.util.exceptions.IOException.extractFrom(e);
        } catch (InterruptedException e) {
            // This thread must stop process before terminating
            process.destroyForcibly();
            return INTERRUPTED_PROCESS_INSTANCE;
        }
    }

    public static final InterruptedProcess INTERRUPTED_PROCESS_INSTANCE = new InterruptedProcess();
    private static class InterruptedProcess extends Process {
        @Override public OutputStream getOutputStream() { return null; }
        @Override public InputStream getInputStream() { return null; }
        @Override public InputStream getErrorStream() { return null; }
        @Override public int waitFor() { throw new IllegalThreadStateException("process is stopped!"); }
        @Override public int exitValue() { throw new IllegalThreadStateException("process is stopped!"); }
        @Override public void destroy() { throw new IllegalThreadStateException("process is stopped!"); }
        @Override public boolean isAlive() { return false; }
    }

    public static final NullProcess NULL_PROCESS_INSTANCE = new NullProcess();
    private static class NullProcess extends Process {
        @Override public OutputStream getOutputStream() { return null; }
        @Override public InputStream getInputStream() { return null; }
        @Override public InputStream getErrorStream() { return null; }
        @Override public int waitFor() { throw new IllegalThreadStateException("process didn\'t start!"); }
        @Override public int exitValue() { throw new IllegalThreadStateException("process didn\'t start!"); }
        @Override public void destroy() {} // I can tell method to throw an exception, but this class was created to
                                           // avoid null-checking when stopping process, so no code here
        @Override public boolean isAlive() { return false; }
    }
}
