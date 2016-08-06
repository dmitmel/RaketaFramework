package org.willthisfly.raketaframework;

import org.willthisfly.raketaframework.util.ExtendedComparator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Objects;

/**
 * Class for storing binary content with MIME.
 */
public class Document extends PrintStream implements Comparable<Document>, Cloneable {
    public final ByteArrayOutputStream outputStream;
    public String mimeType = MIMETypes.BYTE_STREAM;
    
    
    public Document(Document document) {
        this(document.getBytes());
    }
    
    public Document(byte[] bytes) {
        this();
        
        try {
            write(bytes);
        } catch (IOException e) {
            throw new RuntimeException("unreachable code");
        }
    }
    
    public Document() {
        super(new ByteArrayOutputStream());
        outputStream = (ByteArrayOutputStream) out;
    }
    
    
    public int length() {
        return outputStream.size();
    }
    
    public byte[] getBytes() {
        return outputStream.toByteArray();
    }

    @Override
    public String toString() {
        return outputStream.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Document))
            return false;
        Document document = (Document) o;
        return Arrays.equals(this.outputStream.toByteArray(), document.outputStream.toByteArray()) &&
                Objects.equals(this.mimeType, document.mimeType);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(outputStream, mimeType);
    }
    
    @Override
    public Document clone() {
        try {
            return (Document) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException("Document.clone");
        }
    }
    
    @Override
    public int compareTo(Document that) {
        return ExtendedComparator.compareComparingResults(
                ExtendedComparator.compare(this.outputStream, that.outputStream),
                ExtendedComparator.compareNullable(this.mimeType, that.mimeType)
        );
    }
}
