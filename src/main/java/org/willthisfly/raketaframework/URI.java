package org.willthisfly.raketaframework;

import org.willthisfly.raketaframework.util.ExtendedComparator;
import org.willthisfly.raketaframework.util.Strings;
import org.willthisfly.raketaframework.util.SyntaxException;
import org.willthisfly.raketaframework.exceptions.UnsupportedEncodingException;

import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URI implements Comparable<URI>, Cloneable {
    public static final Pattern URI_PATTERN = Pattern.compile(
            "(?:^([^#?]+))" +   // Parsing path (all chars from start of string until '#' or '?')
            "(?:#([^?]*))?" +   // Parsing optional anchor (all chars from '#' until '?')
            "(?:\\?(.*)$)?"     // Parsing optional params (all chars from '?' until end of string)
    );
    public static final int URI_PATTERN_PATH_GROUP = 1;
    public static final int URI_PATTERN_ANCHOR_GROUP = 2;
    public static final int URI_PATTERN_PARAMS_GROUP = 3;
    
    public static final String DEFAULT_PATH = "/";
    public static final Map<String, String> EMPTY_PARAMS = Collections.emptyMap();
    public static final String EMPTY_ANCHOR = Strings.EMPTY_STRING;
    public static final String DEFAULT_PARAM_VALUE = Strings.EMPTY_STRING;

    public static final String UNSAFE_CHARS_IN_PATH = " ?&#";
    public static final String UNSAFE_CHARS_IN_ANCHOR = " ?";
    public static final String UNSAFE_CHARS_IN_PARAMETERS = " &=";

    public final String path;
    public final Map<String, String> params;
    public final String anchor;


    public URI(String path) {
        this(path, EMPTY_PARAMS);
    }

    public URI(String path, Map<String, String> params) {
        this(path, EMPTY_ANCHOR, params);
    }

    public URI(String path, String anchor) {
        this(path, anchor, EMPTY_PARAMS);
    }

    public URI(String path, String anchor, Map<String, String> params) {
        if (path == null || path.trim().isEmpty())
            path = DEFAULT_PATH;
        if (params == null)
            params = EMPTY_PARAMS;
        if (anchor == null || anchor.trim().isEmpty())
            anchor = EMPTY_ANCHOR;

        this.path = path;
        this.params = params;
        this.anchor = anchor;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(URLEncoder.encodeWithUnsafeChars(path, UNSAFE_CHARS_IN_PATH));

        if (!anchor.isEmpty())
            builder.append('#').append(URLEncoder.encodeWithUnsafeChars(anchor, UNSAFE_CHARS_IN_ANCHOR));

        builder.append(paramsToString());

        return builder.toString();
    }

    private String paramsToString() {
        StringBuilder builder = new StringBuilder();

        if (!params.isEmpty()) {
            builder.append('?');

            for (Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, String> entry = iterator.next();
                String encodedKey = URLEncoder.encodeWithUnsafeChars(entry.getKey(), UNSAFE_CHARS_IN_PARAMETERS);
                String encodedValue = URLEncoder.encodeWithUnsafeChars(entry.getValue(), UNSAFE_CHARS_IN_PARAMETERS);

                builder.append(encodedKey).append('=').append(encodedValue);
                if (iterator.hasNext())
                    builder.append('&');
            }
        }

        return builder.toString();
    }


    public static URI fromString(String string) {
        Matcher matcher = URI_PATTERN.matcher(string);

        if (matcher.matches()) {
            String path = decode(matcher.group(URI_PATTERN_PATH_GROUP));
    
            String anchorGroup = matcher.group(URI_PATTERN_ANCHOR_GROUP);
            String anchor = (anchorGroup == null) ? EMPTY_ANCHOR : decode(anchorGroup);
    
            String paramsString = matcher.group(URI_PATTERN_PARAMS_GROUP);
            Map<String, String> paramsMap = (paramsString == null) ? EMPTY_PARAMS : parseParams(paramsString);
    
            return new URI(path, anchor, paramsMap);
        } else {
            throw new SyntaxException("Invalid URI string", string, 0);
        }
    }

    public static Map<String, String> parseParams(String paramsString) {
        Map<String, String> paramsMap = new HashMap<>();

        String[] pairs = (paramsString.isEmpty()) ? (new String[] {}) : (paramsString.split("&"));

        for (String pair : pairs) {
            int equalsSignIndex = pair.indexOf('=');
            
            if (equalsSignIndex >= 0) {
                String key = pair.substring(0, equalsSignIndex);
                String value = pair.substring(equalsSignIndex + 1);
                paramsMap.put(key, value);
            } else {
                paramsMap.put(decode(pair), DEFAULT_PARAM_VALUE);
            }
        }

        return paramsMap;
    }

    /**
     * {@link URLDecoder#decode(String, String)}
     */
    public static String decode(String encoded) {
        try {
            return URLDecoder.decode(encoded, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            throw UnsupportedEncodingException.extractFrom(e);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof URI))
            return false;
        
        URI that = (URI) o;
        return Objects.equals(this.path, that.path) &&
                Objects.equals(this.params, that.params) &&
                Objects.equals(this.anchor, that.anchor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(path, params, anchor);
    }
    
    @Override
    public URI clone() {
        try {
            return (URI) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("unreachable code");
        }
    }
    
    @Override
    public int compareTo(URI that) {
        int i = ExtendedComparator.compareNullable(this.path, that.path);
        if (i != ExtendedComparator.EQUALS)
            return i;
        
        i = ExtendedComparator.compareNullable(this.anchor, that.anchor);
        if (i != ExtendedComparator.EQUALS)
            return i;
        
        return ExtendedComparator.compare(params, params);
    }
}
