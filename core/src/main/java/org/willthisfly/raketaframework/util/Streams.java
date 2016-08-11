package org.willthisfly.raketaframework.util;

import java.io.*;

public class Streams {
    private Streams() {
        throw new UnsupportedOperationException("Can\'t create instance of Streams");
    }
    
    
    public static byte[] readAllWithoutClosing(InputStream inputStream) {
        try {
            if (inputStream instanceof BufferedInputStream) {
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                return buffer;
            } else {
                return readAllWithoutClosing(new BufferedInputStream(inputStream));
            }
        } catch (java.io.IOException e) {
            throw org.willthisfly.raketaframework.exceptions.IOException.extractFrom(e);
        }
    }
    
    public static byte[] readAllWithClosing(InputStream inputStream) {
        try {
            return readAllWithoutClosing(inputStream);
        } finally {
            closeQuietly(inputStream);
        }
    }
    
    public static void writeAllWithClosing(byte[] data, OutputStream outputStream) {
        try {
            writeAllWithoutClosing(data, outputStream);
        } finally {
            closeQuietly(outputStream);
        }
    }
    
    public static void writeAllWithoutClosing(byte[] data, OutputStream outputStream) {
        try {
            if (outputStream instanceof BufferedOutputStream)
                outputStream.write(data);
            else
                writeAllWithoutClosing(data, new BufferedOutputStream(outputStream));
        } catch (java.io.IOException e) {
            throw org.willthisfly.raketaframework.exceptions.IOException.extractFrom(e);
        }
    }
    
    public static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (java.io.IOException ignored) {
            
        }
    }
}
