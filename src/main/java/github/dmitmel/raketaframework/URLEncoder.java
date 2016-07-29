package github.dmitmel.raketaframework;

public class URLEncoder {
    private URLEncoder() {
        throw new RuntimeException("Can\'t create instance of URLEncoder");
    }
    
    
    public static String encodeWithUnsafeChars(String input, String unsafeChars) {
        StringBuilder resultStr = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (isUnsafe(ch) || unsafeChars.indexOf(ch) >= 0) {
                resultStr.append('%');
                resultStr.append(toHex(ch / 16));
                resultStr.append(toHex(ch % 16));
            } else {
                resultStr.append(ch);
            }
        }
        return resultStr.toString();
    }

    private static char toHex(int ch) {
        return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
    }

    private static boolean isUnsafe(char ch) {
        return ch > 128;
    }
}
