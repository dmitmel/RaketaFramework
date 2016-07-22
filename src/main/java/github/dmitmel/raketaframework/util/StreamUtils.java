package github.dmitmel.raketaframework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {
    public static byte[] readAll(InputStream inputStream) {
        try {
            if (inputStream instanceof BufferedInputStream) {
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                return buffer;
            } else {
                return readAll(new BufferedInputStream(inputStream));
            }
        } catch (java.io.IOException e) {
            throw github.dmitmel.raketaframework.util.exceptions.IOException.extractFrom(e);
        } finally {
            try {
                inputStream.close();
            } catch (java.io.IOException e) {
                throw github.dmitmel.raketaframework.util.exceptions.IOException.extractFrom(e);
            }
        }
    }

    public static void writeAll(byte[] data, OutputStream outputStream) {
        try {
            if (outputStream instanceof BufferedOutputStream)
                outputStream.write(data);
            else
                writeAll(data, new BufferedOutputStream(outputStream));
    
        } catch (java.io.IOException e) {
            throw github.dmitmel.raketaframework.util.exceptions.IOException.extractFrom(e);
        } finally {
            try {
                outputStream.close();
            } catch (java.io.IOException e) {
                throw github.dmitmel.raketaframework.util.exceptions.IOException.extractFrom(e);
            }
        }
    }
}
