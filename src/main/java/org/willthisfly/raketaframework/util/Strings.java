package org.willthisfly.raketaframework.util;

import java.util.regex.PatternSyntaxException;

public class Strings {
    public static final String EMPTY_STRING = "";
    public static final char NULL = 0;
    
    public static final String CR = "\r";
    public static final String LF = "\n";
    public static final String CRLF = CR+LF;
    
    
    private Strings() {
        throw new RuntimeException("Can\'t create instance of Strings");
    }
    
    
    public static String copyString(String string, int times) {
        StringBuilder indent = new StringBuilder(string.length() * times);
        for (int i = 0; i < times; i++)
            indent.append(string);
        return indent.toString();
    }
    
    public static char next(CharSequence s, int i) {
        return i + 1 < s.length() ? s.charAt(i + 1) : NULL;
    }
    
    public static String takeBeforeOptional(char target, int offset, String string) {
        StringBuilder result = new StringBuilder();
        String searchingString = string.substring(offset);
        
        for (char c : searchingString.toCharArray()) {
            if (c == target)
                break;
            else
                result.append(c);
        }
        
        return result.toString();
    }
    
    public static String takeBeforeRequired(char target, int offset, String string) {
        String result = takeBeforeOptional(target, offset, string);
        String searchingString = string.substring(offset);
        
        if (result.equals(searchingString))
            throw new PatternSyntaxException("Missing \'"+target+"\'", string, offset);
        
        return result;
    }
    
    public static String takeWhileAllowed(String allowed, int offset, String string) {
        StringBuilder result = new StringBuilder();
        String searchingString = string.substring(offset);
        
        for (char c : searchingString.toCharArray()) {
            if (allowed.indexOf(c) >= 0)
                result.append(c);
            else
                break;
        }
        
        return result.toString();
    }
}
