package org.willthisfly.raketaframework;

import org.willthisfly.raketaframework.util.Arrays;
import org.willthisfly.raketaframework.util.ExtendedComparator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Objects;

/**
 * Class for storing binary content with MIME.
 */
public class Document extends PrintStream implements Iterable<Byte>, Comparable<Document>, Cloneable {
    private ByteArrayOutputStream outputStream;
    public String mimeType = MIMETypes.BYTE_STREAM;
    
    
    public Document(Document document) {
        this(document.getBytes());
    }
    
    public Document(byte[] bytes) {
        this();
        write(bytes);
    }
    
    public Document() {
        super(new ByteArrayOutputStream());
        outputStream = (ByteArrayOutputStream) out;
    }
    
    
    @Override
    public void write(byte[] bytes) {
        try {
            super.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException("unreachable code", e);
        }
    }
    
    public void writeAll(Document document) {
        write(document.getBytes());
    }
    
    public void write(byte b) {
        super.write((int) b);
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
        Document that = (Document) o;
        return java.util.Arrays.equals(this.getBytes(), that.getBytes()) &&
                Objects.equals(this.mimeType, that.mimeType);
    }
    
    @Override
    public int hashCode() {
        int i = java.util.Arrays.hashCode(getBytes());
        i = 31 * i + (mimeType == null ? 0 : mimeType.hashCode());
        return i;
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
    
    @Override
    public Iterator<Byte> iterator() {
        byte[] clonedBytes = getBytes().clone();
        Byte[] boxedBytes = Arrays.box(clonedBytes);
        return Arrays.toIterator(boxedBytes);
    }
}
