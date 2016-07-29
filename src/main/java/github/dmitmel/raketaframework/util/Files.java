package github.dmitmel.raketaframework.util;

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
     * Function makes real path to file. But,
     * <span color="#A52500"><b>YOU HAVE TO USE SINGLE SLASHES
     * AS FILE SEPARATORS!</b></span>
     */
    public static String realPath(String relative) {
        try {
            List<String> relativePathParts = splitPath(relative);

            // Caller class is on third stack trace element, on second is this function, and on first -
            // "Thread#getStackTrace()".
            Class<?> callerClass = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
            List<String> callerClassPathParts = splitPath(Reflection.getPathToClass(callerClass));

            List<String> realPathParts = new ArrayList<>();
            for (int i = 0; i < relativePathParts.size(); i++) {
                String part = relativePathParts.get(i);

                if ((".".equals(part) || "./".equals(part)) && i == 0) {
                    // If current directory marker is first item
                    realPathParts.addAll(callerClassPathParts);
                } else if ("..".equals(part) || "../".equals(part)) {
                    if (i == 0)
                        // If upper directory marker is first item
                        realPathParts.addAll(callerClassPathParts);
                    realPathParts.remove(realPathParts.size() - 1);
                } else {
                    realPathParts.add(part);
                }
            }

            return String.join(Strings.EMPTY_STRING, realPathParts);
        } catch (ClassNotFoundException e) {
            // Isn't reachable in this case
            throw new RuntimeException();
        }
    }
    
    /**
     * Splits path fragments. For example, path
     * <pre><code>/usr/lib/jvm/java-8-oracle/jre/lib/ext/</code></pre>
     * will be split to list
     * <pre><code>["/", "usr/", "lib/", "jvm/", "java-8-oracle/", "jre/", "lib/", "ext/"]</code></pre>
     *
     * And Windows-Style path
     * <pre><code>C:/gcc/gcc-4.8.0/libjava/javax/xml/parsers</code></pre>
     * will be split to list
     * <pre><code>["C:/", "gcc/", "gcc-4.8.0/", "libjava/", "javax/", "xml/", "parsers"]</code></pre>
     *
     * Function also supports file names and you don't have to add slash at the end of string to say that the last
     * element is a directory.
     *
     * @param path path to process
     * @return list of path parts.
     */
    private static List<String> splitPath(String path) {
        List<String> out = new ArrayList<>();
        
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            buffer.append(c);
            
            if (c == '/' || i + 1 == path.length()) {
                out.add(buffer.toString());
                buffer = new StringBuilder();
            }
        }
        
        return out;
    }

    public static String read(String path) { return new String(readBytes(path)); }
    public static byte[] readBytes(String path) {
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(path));
            return Streams.readAllWithClosing(inputStream);
        } catch (java.io.FileNotFoundException e) {
            throw github.dmitmel.raketaframework.exceptions.FileNotFoundException.extractFrom(e);
        }
    }
    
    public static void write(String path, String lines) { writeBytes(path, lines.getBytes()); }
    public static void writeBytes(String path, byte[] bytes) {
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
            Streams.writeAllWithClosing(bytes, outputStream);
        } catch (java.io.FileNotFoundException e) {
            throw github.dmitmel.raketaframework.exceptions.FileNotFoundException.extractFrom(e);
        }
    }
}
