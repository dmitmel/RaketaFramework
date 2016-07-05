package github.dmitmel.raketaframework.web.handle;

import github.dmitmel.raketaframework.web.MIMETypes;

import java.util.Arrays;

/**
 * Class for documents which are being built by request handlers. Stores binary data, so you can put even image.
 */
public class Document {
    private byte[] bytes = new byte[0];
    public byte[] getBytes() {
        return bytes;
    }

    public String mimeType = MIMETypes.PLAIN_TEXT;

    public Document() {}

    public void write(byte[] newData) {
        for (byte b : newData) {
            byte[] savedData = Arrays.copyOf(bytes, bytes.length);
            bytes = new byte[savedData.length + 1];
            // Copy all elements of savedData, starting from index 0, to starting of bytes
            System.arraycopy(savedData, 0, bytes, 0, savedData.length);
            bytes[savedData.length] = b;
        }
    }

    public void write(String newData) { write(newData.getBytes()); }
    public void writeln(String newData) { write(newData + "\n"); }
    public void writeF(String newData, Object... params) { write(String.format(newData, params)); }
}
