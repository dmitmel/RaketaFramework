package github.dmitmel.raketaframework.util;

import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {
    public static byte[] readAll(InputStream inputStream) {
        try {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            return buffer;
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
            outputStream.write(data);
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
