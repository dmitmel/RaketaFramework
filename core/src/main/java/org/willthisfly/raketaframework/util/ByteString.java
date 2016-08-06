package org.willthisfly.raketaframework.util;

import java.util.Iterator;
import java.util.stream.IntStream;

/**
 * Byte-analog of {@link String} which has most of {@link String}'s
 * methods.
 */
public class ByteString implements CharSequence, Iterable<Byte>, Comparable<ByteString>, Cloneable {
    public static final byte[] EMPTY_BYTES = new byte[0];
    public static final ByteString EMPTY = new ByteString();
    
    
    public final byte[] bytes;
    
    public ByteString() {
        this(EMPTY_BYTES);
    }
    
    public ByteString(byte[] bytes) {
        this.bytes = bytes;
    }
    
    public ByteString(ByteString other) {
        this(other.bytes);
    }
    
    public ByteString(String string) {
        this(string.getBytes());
    }
    
    
    public ByteString(CharSequence charSequence) {
        IntStream charsStream = charSequence.chars();
        int[] charsArray = charsStream.toArray();
        
        this.bytes = new byte[charsArray.length];
        for (int i = 0; i < charsArray.length; i++)
            this.bytes[i] = (byte) charsArray[i];
    }
    
    @Override
    public int length() {
        return bytes.length;
    }
    
    public boolean isEmpty() {
        return length() == 0;
    }
    
    public boolean isWhitespace() {
        return trim().isEmpty();
    }
    
    @Override
    public char charAt(int index) {
        return (char) byteAt(index);
    }
    
    public byte byteAt(int index) {
        return bytes[index];
    }
    
    @Override
    public CharSequence subSequence(int start, int end) {
        if (start > end)
            throw new IllegalArgumentException(String.format("start(%d) > end(%d)", start, end));
        
        byte[] result = new byte[end - start];
        for (int i = start; i < end; i++)
            result[i - start] = byteAt(i);
        return new ByteString(result);
    }
    
    @Override
    public Iterator<Byte> iterator() {
        return new Iterator<Byte>() {
            int cursor = 0;
            
            @Override
            public boolean hasNext() {
                return cursor < length();
            }
    
            @Override
            public Byte next() {
                cursor++;
                return byteAt(cursor);
            }
        };
    }
    
    @Override
    public int compareTo(ByteString other) {
        int firstLength = this.length();
        int secondLength = other.length();
        int limit = Math.min(firstLength, secondLength);
    
        for (int i = 0; i < limit; i++) {
            byte firstByte = this.byteAt(i);
            byte secondByte = other.byteAt(i);
            if (firstByte != secondByte)
                return firstByte - secondByte;
        }
        
        return firstLength - secondLength;
    }
    
    public int compareToIgnoreCase(ByteString other) {
        return this.toLowerCase().compareTo(other.toLowerCase());
    }
    
    @Override
    public String toString() {
        return new String(bytes);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ByteString))
            return false;
        
        ByteString that = (ByteString) o;
        return java.util.Arrays.equals(this.bytes, that.bytes);
    }
    
    @Override
    public int hashCode() {
        return java.util.Arrays.hashCode(bytes);
    }
    
    @Override
    public ByteString clone() {
        try {
            return (ByteString) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException("ByteString.clone");
        }
    }
    
    public boolean contentEquals(CharSequence charSequence) {
        return this.equals(new ByteString(charSequence));
    }
    
    public boolean equalsIgnoreCase(ByteString other) {
        return this.toLowerCase().equals(other.toLowerCase());
    }
    
    public boolean startsWith(ByteString prefix) {
        boolean startsWith = true;
    
        for (int i = 0; i < this.length() && startsWith; i++)
            if (this.byteAt(i) != prefix.byteAt(i))
                startsWith = false;
        
        return startsWith;
    }
    
    public boolean endsWith(ByteString suffix) {
        boolean endsWith = true;
    
        for (int i = this.length() - 1; i >= 0 && endsWith; i--)
            if (this.byteAt(i) != suffix.byteAt(i))
                endsWith = false;
    
        return endsWith;
    }
    
    public int indexOf(byte b) {
        for (int i = 0; i < this.length(); i++)
            if (this.byteAt(i) == b)
                return i;
        return -1;
    }
    
    public int lastIndexOf(byte b) {
        for (int i = this.length() - 1; i >= 0; i--)
            if (this.byteAt(i) == b)
                return i;
        return -1;
    }
    
    public int indexOf(ByteString target) {
        if (target.length() == 0)
            return 0;
    
        outer:
        for (int startIndex = 0; startIndex < this.length() - target.length() + 1; startIndex++) {
            for (int i = 0; i < target.length(); i++)
                if (this.charAt(startIndex + i) != target.charAt(i))
                    continue outer;
            return startIndex;
        }
    
        return -1;
    }
    
    public int lastIndexOf(ByteString target) {
        if (target.length() == 0)
            return 0;
        
        int lastIndex = -1;
    
        outer:
        for (int startIndex = 0; startIndex < this.length() - target.length() + 1; startIndex++) {
            for (int i = 0; i < target.length(); i++)
                if (this.charAt(startIndex + i) != target.charAt(i))
                    continue outer;
            lastIndex = startIndex;
        }
    
        return lastIndex;
    }
    
    public ByteString substring(int beginIndex) {
        return substring(beginIndex, length());
    }
    
    public ByteString substring(int beginIndex, int endIndex) {
        return new ByteString(subSequence(beginIndex, endIndex));
    }
    
    public ByteString concat(ByteString other) {
        byte[] joined = new byte[this.length() + other.length()];
        System.arraycopy(this.bytes, 0, joined, 0, this.length());
        System.arraycopy(other.bytes, 0, joined, this.length(), other.length());
        return new ByteString(joined);
    }
    
    public ByteString replace(byte oldByte, byte newByte) {
        byte[] result = new byte[length()];
        
        for (int i = 0; i < length(); i++) {
            byte current = byteAt(i);
            
            if (current == oldByte)
                result[i] = newByte;
            else
                result[i] = current;
        }
        
        return new ByteString(result);
    }
    
    public boolean contains(CharSequence s) {
        return indexOf(new ByteString(s)) != -1;
    }
    
    public ByteString[] split(ByteString delimiter) {
        byte[][] sequences = Arrays.splitArray(this.bytes, delimiter.bytes);
        return java.util.Arrays.stream(sequences)
                .map(ByteString::new)
                .toArray(ByteString[]::new);
    }
    
    public ByteString[] split(String delimiter) {
        return split(new ByteString(delimiter));
    }
    
    public ByteString toLowerCase() {
        byte[] result = new byte[length()];
        for (int i = 0; i < length(); i++)
            result[i] = (byte) Character.toLowerCase(byteAt(i));
        return new ByteString(result);
    }
    
    public ByteString toUpperCase() {
        byte[] result = new byte[length()];
        for (int i = 0; i < length(); i++)
            result[i] = (byte) Character.toUpperCase(byteAt(i));
        return new ByteString(result);
    }
    
    public ByteString trim() {
        int trimmedLength = length();
        int trimmedStart = 0;
    
        while (trimmedStart < trimmedLength && Character.isWhitespace(charAt(trimmedStart)))
            trimmedStart++;
        while (trimmedStart < trimmedLength && Character.isWhitespace(charAt(trimmedLength - 1)))
            trimmedLength--;
        
        return ((trimmedStart > 0) || (trimmedLength < length())) ? substring(trimmedStart, trimmedLength) : this;
    }
    
    public char[] toCharArray() {
        return toString().toCharArray();
    }
}
