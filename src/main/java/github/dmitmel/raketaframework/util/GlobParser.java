package github.dmitmel.raketaframework.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class GlobParser {
    private static final String REGEXP_META_CHARS = ".^$+{[]|()";
    private static boolean isRegexMeta(char c) { return REGEXP_META_CHARS.indexOf(c) != -1; }
    
    private static final String GLOB_META_CHARS = "\\*?[{";
    private static boolean isGlobMeta(char c) { return GLOB_META_CHARS.indexOf(c) != -1; }
    
    private static final String ALPHANUMERIC_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    private static final String BOX_DRAWING_CHARS_START = "\\u2500";
    private static final String BOX_DRAWING_CHARS_END = "\\u257F";
    
    
    private GlobParser() {
        throw new RuntimeException("Can\'t create instance of GlobParser");
    }
    
    
    private static final Map<String, String> BUILT_IN_CHAR_CLASSES = Maps.unmodifiableMap(
            // Alphanumeric characters
            new SimpleEntry<>(":alnum:", "a-zA-Z0-9"),
            // Alphabetic characters
            new SimpleEntry<>(":alpha:", "a-zA-Z"),
            // Blank characters (don't confuse with space characters!)
            new SimpleEntry<>(":blank:", "\\t "),
            // Control characters
            new SimpleEntry<>(":cntrl:", "\\a\\f\\n\\r\\t"),
            // Decimal digits
            new SimpleEntry<>(":digit:", "0-9"),
            // Graphic characters
            new SimpleEntry<>(":graph:", String.format("%s-%s", BOX_DRAWING_CHARS_START, BOX_DRAWING_CHARS_END)),
            // Lowercase letters
            new SimpleEntry<>(":lower:", "a-z"),
            // Printing characters
            new SimpleEntry<>(":print:", " !\"#$%&\'()*+,-./:;<=>?@\\[\\\\\\]^_`\\{|}~a-zA-Z"),
            // Punctuation characters
            new SimpleEntry<>(":punct:", "!\"#$%&\'()*+,-./:;<=>?@\\[\\\\\\]^_`\\{|}~"),
            // Space characters (don't confuse with blank characters!)
            new SimpleEntry<>(":space:", "\\f\\n\\r\\t\\v "),
            // Uppercase letters
            new SimpleEntry<>(":upper:", "A-Z"),
            // Hexadecimal digits
            new SimpleEntry<>(":xdigit:", "0-9a-fA-F")
    );
    
    private static final HashMap<String, Pattern> GLOB_CACHE = new HashMap<>();
    
    public static Pattern globToPatternWithCaching(String glob) {
        if (GLOB_CACHE.containsKey(glob)) {
            return GLOB_CACHE.get(glob);
        } else {
            Pattern pattern = globToPattern(glob);
            GLOB_CACHE.put(glob, pattern);
            return pattern;
        }
    }
    
    public static Pattern globToPattern(String glob) {
        boolean readingGroup = false;
        StringBuilder patternBuilder = new StringBuilder("^");
        
        for (int i = 0; i < glob.length(); i++) {
            char c = glob.charAt(i);
            
            switch (c) {
                case ':':
                    String groupName = Strings.takeWhileAllowed(ALPHANUMERIC_CHARS, i + 1, glob);
                    
                    if (groupName.isEmpty())
                        throw new PatternSyntaxException("Empty group name", glob, i);
                    
                    String appending = "(?<"+groupName+">[^/]+)";
                    patternBuilder.append(appending);
                    i += groupName.length();
                    
                    break;
                
                case '*':
                    patternBuilder.append('(');
                    
                    if (Strings.next(glob, i) == '*') {
                        patternBuilder.append(".*");
                        i++;
                    } else {
                        patternBuilder.append("[^/]*");
                    }
                    
                    patternBuilder.append(')');
                    
                    break;
                
                case '/':
                    patternBuilder.append(c);
                    break;
                
                case '?':
                    patternBuilder.append("[^/]");
                    break;
                
                case '[':
                    patternBuilder.append("[[^/]&&[");
                    
                    int offset = i + 1;
                    if (Strings.next(glob, i) == '!') {
                        patternBuilder.append('^');
                        offset++;
                    }
                    
                    String charClass = Strings.takeBeforeRequired(']', offset, glob);
                    
                    // Validation
                    if (charClass.isEmpty())
                        throw new PatternSyntaxException("Empty character class", glob, i);
                    if (charClass.contains("/"))
                        throw new PatternSyntaxException("Explicit \'name separator\' in character class", glob,
                                offset + charClass.indexOf('/'));
                    if (charClass.charAt(0) == '-')
                        throw new PatternSyntaxException("Invalid range", glob, i + 1);
                    if (charClass.charAt(charClass.length() - 1) == '-')
                        throw new PatternSyntaxException("Invalid range", glob, i + charClass.length());
                    
                    if (charClass.charAt(0) == '^')
                        charClass = '\\' + charClass;
                    charClass = charClass.replace("\\", "\\\\").replace("[", "\\[").replace("&&", "\\&&");
                    
                    // Saving char class length before replacing it with built-in class if needed
                    int realCharClassLength = charClass.length();
                    
                    if (BUILT_IN_CHAR_CLASSES.containsKey(charClass))
                        charClass = BUILT_IN_CHAR_CLASSES.get(charClass);
                    
                    patternBuilder.append(charClass).append("]]");
                    
                    // Adding offset to make 'i' point to class closing bracket. 'i' will be incremented in the next
                    // iteration, so it'll point to char after closing bracket
                    i += offset + realCharClassLength;
                    break;
                
                case '\\':
                    char nextChar = Strings.next(glob, i);
                    
                    if (nextChar == Strings.NULL)
                        throw new PatternSyntaxException("No character to escape", glob, i);
                    
                    if (isGlobMeta(nextChar) || isRegexMeta(nextChar))
                        patternBuilder.append('\\');
                    
                    patternBuilder.append(nextChar);
                    
                    i++;
                    break;
                
                case '{':
                    if (readingGroup)
                        throw new PatternSyntaxException("Cannot nest groups", glob, i);
                    
                    // Adding 2 positive search starters to allow multiple variants in groups
                    patternBuilder.append("(?:(?:");
                    readingGroup = true;
                    break;
                
                case ',':
                    if (readingGroup)
                        patternBuilder.append(")|(?:");
                    else
                        patternBuilder.append(',');
                    break;
                
                case '}':
                    if (readingGroup) {
                        patternBuilder.append("))");
                        readingGroup = false;
                    } else {
                        patternBuilder.append('}');
                    }
                    
                    break;
                
                default:
                    if (isRegexMeta(c))
                        patternBuilder.append('\\');
                    
                    patternBuilder.append(c);
            }
        }
        
        if (readingGroup)
            throw new PatternSyntaxException("Missing \'}\'", glob, glob.length() - 1);
        
        patternBuilder.append('$');
        return Pattern.compile(patternBuilder.toString());
    }
}
