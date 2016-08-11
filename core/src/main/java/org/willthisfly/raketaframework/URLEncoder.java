package org.willthisfly.raketaframework;

public class URLEncoder {
    private static final String DEFAULT_SAFE_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "!$%&()*+,-./:;=?@\\[]^_\\{|}~";
    private static boolean isSafe(char ch) { return DEFAULT_SAFE_CHARS.indexOf(ch) >= 0; }
    
    
    private URLEncoder() {
        throw new UnsupportedOperationException("Can\'t create instance of URLEncoder");
    }
    
    
    public static String encodeWithUnsafeChars(String input, String unsafeChars) {
        StringBuilder resultStr = new StringBuilder();
        
        for (char ch : input.toCharArray()) {
            if (!isSafe(ch) || unsafeChars.indexOf(ch) >= 0) {
                resultStr.append('%');
                resultStr.append(Integer.toHexString(ch));
            } else {
                resultStr.append(ch);
            }
        }
        
        return resultStr.toString();
    }
}
