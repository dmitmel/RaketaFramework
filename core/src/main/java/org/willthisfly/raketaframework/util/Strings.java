package org.willthisfly.raketaframework.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.PatternSyntaxException;

public class Strings {
    public static final String EMPTY_STRING = "";
    public static final char NULL = 0;
    
    public static final String CR = "\r";
    public static final String LF = "\n";
    public static final String CRLF = CR+LF;
    
    
    private Strings() {
        throw new UnsupportedOperationException("Can\'t create instance of Strings");
    }
    
    
    public static String copy(CharSequence charSequence, int times) {
        StringBuilder indent = new StringBuilder(charSequence.length() * times);
        for (int i = 0; i < times; i++)
            indent.append(charSequence);
        return indent.toString();
    }
    
    public static char next(CharSequence s, int i) {
        return i + 1 < s.length() ? s.charAt(i + 1) : NULL;
    }
    
    public static String takeWhileAllowed(String allowed, int offset, CharSequence charSequence) {
        StringBuilder result = new StringBuilder();
        CharSequence searchingSequence = charSequence.subSequence(offset, charSequence.length());
        
        for (char c : toIterable(searchingSequence)) {
            if (allowed.indexOf(c) >= 0)
                result.append(c);
            else
                break;
        }
        
        return result.toString();
    }
    
    public static char last(CharSequence charSequence) {
        int length = charSequence.length();
        if (length == 0)
            return NULL;
        else
            return charSequence.charAt(length - 1);
    }
    
    public static String withoutLastChar(CharSequence charSequence) {
        return toString(charSequence.subSequence(0, charSequence.length() - 1));
    }
    
    public static String removeTerminator(char terminator, CharSequence charSequence) {
        char last = last(charSequence);
        if (last == terminator)
            return withoutLastChar(charSequence);
        else
            return toString(charSequence);
    }
    
    public static String toString(CharSequence charSequence) {
        StringBuilder builder = new StringBuilder();
        CharSequence subSequence = charSequence.subSequence(0, charSequence.length() - 1);
        for (char c : toIterable(subSequence))
            builder.append(c);
        return builder.toString();
    }
    
    public static Iterable<Character> toIterable(CharSequence charSequence) {
        return () -> new CharacterIterator(charSequence);
    }
    
    
    private static class CharacterIterator implements Iterator<Character> {
        private CharSequence charSequence;
        private int cursor = 0;
        
        
        public CharacterIterator(CharSequence charSequence) {
            this.charSequence = charSequence;
        }
        
    
        @Override
        public boolean hasNext() {
            return cursor < charSequence.length();
        }
    
        @Override
        public Character next() {
            if (hasNext()) {
                Character result = charSequence.charAt(cursor);
                cursor++;
                return result;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
