package github.dmitmel.raketaframework.util;

import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoreFiles {
    /**
     * Splits path fragments. For example, path (randomly selected, needed big nesting)
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
        List<String> out = new ArrayList<>(0);

        StringBuilder buffer = new StringBuilder(0);
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            buffer.append(c);

            if (c == '/' || i + 1 == path.length()) {
                out.add(buffer.toString());
                buffer = new StringBuilder(0);
            }
        }

        return out;
    }

    /**
     * Function makes real path to file.
     *
     * @param relative path to translate to real. But,
     *                 <span color="#A52500"><b>YOU HAVE TO USE SLASHES AS FILE SEPARATORS!</b></span>
     * @return constructed real path.
     */
    public static String realPath(String relative) {
        try {
            List<String> relativePathParts = splitPath(relative);

            // Caller class is on third stack trace element, on second is this function, and on first -
            // "Thread#getStackTrace()".
            Class<?> callerClass = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
            List<String> callerClassPathParts = splitPath(ReflectionUtils.getPathToClass(callerClass));

            List<String> realPathParts = new ArrayList<>(0);
            for (int i = 0; i < relativePathParts.size(); i++) {
                String part = relativePathParts.get(i);

                if ((part.equals("./") || part.equals(".")) && i == 0) {
                    // If current directory marker is first item
                    realPathParts.addAll(callerClassPathParts);
                } else if (part.equals("../") || part.equals("..")) {
                    if (i == 0)
                        // If upper directory marker is first item
                        realPathParts.addAll(callerClassPathParts);
                    realPathParts.remove(realPathParts.size() - 1);
                } else {
                    realPathParts.add(part);
                }
            }

            return IterableUtils.join(realPathParts, StringUtils.EMPTY_STRING);
        } catch (ClassNotFoundException e) {
            // Isn't reachable in this case
            throw new RuntimeException();
        }
    }

    public static String load(String path) {
        StringBuilder fileData = new StringBuilder(0);
        File file = new File(path);
        FileReader fileReader = null;
        BufferedReader buffer = null;

        try {
            fileReader = new FileReader(file);
            buffer = new BufferedReader(fileReader);

            for (String line = StringUtils.EMPTY_STRING; line != null; line = buffer.readLine())
                fileData.append(line).append('\n');
        } catch (java.io.IOException e) {
             throw new github.dmitmel.raketaframework.util.exceptions.IOException(e);
        } finally {
            try {
                if (fileReader != null)
                    fileReader.close();
                if (buffer != null)
                    buffer.close();
            } catch (java.io.IOException e) {
                throw new github.dmitmel.raketaframework.util.exceptions.IOException(e);
            }
        }

        return fileData.toString();
    }

    public static void write(String path, String lines) {
        File file = new File(path);
        FileWriter fileWriter = null;
        BufferedWriter buffer = null;

        try {
            fileWriter = new FileWriter(file);
            buffer = new BufferedWriter(fileWriter);
            buffer.write(lines);
        } catch (java.io.IOException e) {
            throw new github.dmitmel.raketaframework.util.exceptions.IOException(e);
        } finally {
            try {
                if (buffer != null) {
                    buffer.flush();
                    buffer.close();
                }
                if (fileWriter != null)
                    fileWriter.close();
            } catch (java.io.IOException e) {
                throw new github.dmitmel.raketaframework.util.exceptions.IOException(e);
            }
        }
    }
}
