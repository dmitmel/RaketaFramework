package org.willthisfly.raketaframework;

import org.willthisfly.raketaframework.exceptions.UnsupportedEncodingException;
import org.willthisfly.raketaframework.util.ExtendedComparator;
import org.willthisfly.raketaframework.util.Strings;

import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Pattern;

public class URI implements Comparable<URI>, Cloneable {
    public static final String EMPTY_PATH = "/";
    public static final Map<String, String> EMPTY_PARAMS = Collections.emptyMap();
    public static final String EMPTY_ANCHOR = Strings.EMPTY_STRING;
    public static final String EMPTY_PARAM_VALUE = Strings.EMPTY_STRING;

    private static final String UNSAFE_CHARS_IN_PATH = " ?&#";
    private static final String UNSAFE_CHARS_IN_ANCHOR = " ?";
    private static final String UNSAFE_CHARS_IN_PARAMETERS = " &=";

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
            path = EMPTY_PATH;
        if (params == null)
            params = EMPTY_PARAMS;
        if (anchor == null || anchor.trim().isEmpty())
            anchor = EMPTY_ANCHOR;

        this.path = path;
        this.params = params;
        this.anchor = anchor;
    }
    
    
    public static URI parse(CharSequence charSequence) {
        return URIParser.parse(charSequence);
    }
    
    public static Map<String, String> parseURILikeParams(String paramsString) {
        return URIParser.parseURILikeParams(paramsString);
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
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(URLEncoder.encodeWithUnsafeChars(path, UNSAFE_CHARS_IN_PATH));

        if (!anchor.isEmpty()) {
            String encodedAnchor = URLEncoder.encodeWithUnsafeChars(anchor, UNSAFE_CHARS_IN_ANCHOR);
            builder.append('#').append(encodedAnchor);
        }

        builder.append(paramsToString());

        return builder.toString();
    }

    private String paramsToString() {
        if (!params.isEmpty()) {
            StringJoiner result = new StringJoiner("&");
    
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String encodedKey = URLEncoder.encodeWithUnsafeChars(entry.getKey(), UNSAFE_CHARS_IN_PARAMETERS);
                String encodedValue = URLEncoder.encodeWithUnsafeChars(entry.getValue(), UNSAFE_CHARS_IN_PARAMETERS);
        
                result.add(encodedKey + '=' + encodedValue);
            }
            
            return '?' + result.toString();
        } else {
            return Strings.EMPTY_STRING;
        }
    }
    
    public int length() {
        return path.length() +
                (anchor.isEmpty() ? 0 : 1 + anchor.length()) +
                paramsLength();
    }
    
    private int paramsLength() {
        if (params.isEmpty())
            return 0;
        else
            return 1 + params.entrySet().stream()
                    // Adding 1 after each entry to represent '&' character. Last increment will be removed
                    .mapToInt(entry -> entry.getKey().length() + 1 + entry.getValue().length() + 1)
                    .sum() - 1;   // Subtracting 1 to remove last increment for '&'
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
            throw new UnsupportedOperationException("URI.clone");
        }
    }
    
    @Override
    public int compareTo(URI that) {
        return ExtendedComparator.compareComparingResults(
                ExtendedComparator.compareNullable(this.path, that.path),
                ExtendedComparator.compareNullable(this.anchor, that.anchor),
                ExtendedComparator.compare(this.params, that.params)
        );
    }
}
