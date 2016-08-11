package org.willthisfly.raketaframework.util;

import org.willthisfly.raketaframework.exceptions.FileNotFoundException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Files {
    private Files() {
        throw new RuntimeException("Can\'t create instance of Files");
    }
    
    
    /**
     * Function makes real path to file. But, <b>YOU HAVE TO USE
     * SINGLE SLASHES AS FILE SEPARATORS!</b>
     */
    public static String realPath(String relative) {
        List<String> relativePathParts = splitPath(relative);
    
        List<String> callerDirectoryParts = splitPath(getCallerClassDirectory());
    
        List<String> realPathParts = new ArrayList<>();
    
        for (int i = 0; i < relativePathParts.size(); i++) {
            String part = relativePathParts.get(i);

            if ((".".equals(part) || "./".equals(part)) && i == 0) {
                // If current directory marker is first item
                realPathParts.addAll(callerDirectoryParts);
            } else if ("..".equals(part) || "../".equals(part)) {
                if (i == 0)
                    // If upper directory marker is first item
                    realPathParts.addAll(callerDirectoryParts);
                realPathParts.remove(realPathParts.size() - 1);
            } else {
                realPathParts.add(part);
            }
        }
    
        String result = String.join(Strings.EMPTY_STRING, realPathParts);
        return Strings.removeTerminator('/', result);
    }
    
    /**
     * Gets caller class directory with terminating slash.
     */
    private static String getCallerClassDirectory() {
        try {
            // First elements in the stack trace:
            // [0] = Thread#currentThread
            // [1] = Files#getCallerClass
            // [2] = Files#realPath
            // [3] = <callerClass>#<callerMethod>
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StackTraceElement callerElement = stackTrace[3];
            Class<?> callerLocation = Class.forName(callerElement.getClassName());
            return Reflection.getClassDirectory(callerLocation) + '/';
        } catch (ClassNotFoundException e) {
            System.err.println("[WARNING]: Can\'t get caller class location. Current working directory will be used " +
                    "instead.");
            return System.getProperty("user.dir") + '/';
        }
    }
    
    /**
     * Splits path to fragments.
     *
     * <table>
     *     <caption>Examples</caption>
     *     <tr> <th>Input</th>                      <th>Result</th>                               </tr>
     *     <tr> <td>/home/user/docs/Letter.txt</td> <th>[/, home/, user/, docs/, Letter.txt]</th> </tr>
     *     <tr> <td>C:/home/user/docs/</td>         <td>[C:/, home/, user/, docs/]</td>           </tr>
     * </table>
     */
    private static List<String> splitPath(String path) {
        List<String> result = new ArrayList<>();
        
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            builder.append(c);
    
            if (c == '/' || i + 1 == path.length()) {
                result.add(builder.toString());
                builder = new StringBuilder();
            }
        }
        
        return result;
    }

    public static String read(String path) { return new String(readBytes(path)); }
    public static byte[] readBytes(String path) {
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(path));
            return Streams.readAllWithClosing(inputStream);
        } catch (java.io.FileNotFoundException e) {
            throw FileNotFoundException.extractFrom(e);
        }
    }
    
    public static void write(String path, String lines) { writeBytes(path, lines.getBytes()); }
    public static void writeBytes(String path, byte[] bytes) {
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
            Streams.writeAllWithClosing(bytes, outputStream);
        } catch (java.io.FileNotFoundException e) {
            throw FileNotFoundException.extractFrom(e);
        }
    }
}
