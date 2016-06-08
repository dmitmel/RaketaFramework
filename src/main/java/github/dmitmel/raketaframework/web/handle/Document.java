package github.dmitmel.raketaframework.web.handle;

import github.dmitmel.raketaframework.web.MimeTypes;

/**
 * Class for documents that are being built by request handlers.
 */
public class Document {
    private StringBuilder data = new StringBuilder(0);
    public String getData() {
        return data.toString();
    }

    private String mimeType = MimeTypes.PLAIN_TEXT;
    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Document() {

    }

    public void write(String newData) { data.append(newData); }
    public void writeln(String newData) { write(newData + "\n"); }
    public void writeF(String newData, Object... params) { write(String.format(newData, params)); }
}
