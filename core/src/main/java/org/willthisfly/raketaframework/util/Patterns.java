package org.willthisfly.raketaframework.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patterns {
    private Patterns() {
        throw new UnsupportedOperationException("Can\'t create instance of Patterns");
    }
    
    
    private static final HashMap<Pattern, MatcherCacheValue> MATCHER_CACHE = new HashMap<>();
    
    public static Matcher matcherWithCaching(Pattern pattern, String text) {
        if (MATCHER_CACHE.containsKey(pattern)) {
            MatcherCacheValue value = MATCHER_CACHE.get(pattern);
            
            if (value.text.equals(text)) {
                return value.matcher;
            } else {
                Matcher matcher = pattern.matcher(text);
                MATCHER_CACHE.put(pattern, new MatcherCacheValue(text, matcher));
                return matcher;
            }
                
        } else {
            Matcher matcher = pattern.matcher(text);
            MATCHER_CACHE.put(pattern, new MatcherCacheValue(text, matcher));
            return matcher;
            
        }
    }
    
    public static String[] getNamedGroupsNames(Matcher matcher) {
        ArrayList<String> namedGroups = new ArrayList<>();
        
        Pattern pattern = matcher.pattern();
        String regexp = pattern.pattern();
        
        Matcher groupsMatcher = Pattern.compile("\\(\\?<([a-zA-Z][a-zA-Z0-9]+)>").matcher(regexp);
        
        while (groupsMatcher.find()) {
            String group = groupsMatcher.group(1);
            // That's why I need matcher
            if (matcher.group(group) != null)
                namedGroups.add(group);
        }
        
        return namedGroups.toArray(new String[0]);
    }
    
    private static class MatcherCacheValue {
        private final String text;
        private final Matcher matcher;
    
        public MatcherCacheValue(String text, Matcher matcher) {
            this.text = text;
            this.matcher = matcher;
        }
    }
}
