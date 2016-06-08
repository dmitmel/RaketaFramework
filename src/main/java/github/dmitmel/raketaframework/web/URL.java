package github.dmitmel.raketaframework.web;

import github.dmitmel.raketaframework.util.*;

import java.net.URLDecoder;
import java.util.*;

public class URL {
    public static final String DEFAULT_PATH = "/";
    public static final Map<String, String> EMPTY_PARAMS = Collections.emptyMap();
    public static final String EMPTY_ANCHOR = StringUtils.EMPTY_STRING;
    public static final String DEFAULT_PARAM_VALUE = StringUtils.EMPTY_STRING;

    public static final String UNSAFE_CHARS_IN_PATH = " ?&#";
    public static final String UNSAFE_CHARS_IN_ANCHOR = " ?";
    public static final String UNSAFE_CHARS_IN_PARAMETERS = " &=";

    public final String path;
    public final Map<String, String> params;
    public final String anchor;


    public URL(String path) {
        this(path, EMPTY_PARAMS);
    }

    public URL(String path, Map<String, String> params) {
        this(path, EMPTY_ANCHOR, params);
    }

    public URL(String path, String anchor) {
        this(path, anchor, EMPTY_PARAMS);
    }

    public URL(String path, String anchor, Map<String, String> params) {
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
        StringBuilder builder = new StringBuilder(0);

        builder.append(URLEncoder.encodeWithUnsafeChars(path, UNSAFE_CHARS_IN_PATH));

        if (!anchor.isEmpty())
            builder.append('#').append(URLEncoder.encodeWithUnsafeChars(anchor, UNSAFE_CHARS_IN_ANCHOR));

        builder.append(paramsToString());

        return builder.toString();
    }

    private String paramsToString() {
        StringBuilder builder = new StringBuilder(0);

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


    public static URL fromString(String string) {
        String[] pathWithAnchorAndParams = string.split("\\?");
        String[] pathAndAnchor = pathWithAnchorAndParams[0].split("#");

        String path = decodeFromUrlFormat(pathAndAnchor[0]);
        String anchor = decodeFromUrlFormat(ArrayUtils.getAtIndexOrElse(pathAndAnchor, 1, EMPTY_ANCHOR));
        Map<String, String> paramsMap = parseUrlParams(ArrayUtils.getAtIndexOrElse(pathWithAnchorAndParams, 1,
                // Function #parseUrlParams(String) firstly splits string to pairs by "&", so if string is
                // "&" - no pairs will be found ("a".split("a").length == 0)
                "&"));

        return new URL(path, anchor, paramsMap);
    }

    public static Map<String, String> parseUrlParams(String paramsString) {
        Map<String, String> paramsMap = new HashMap<>(0);

        String[] pairs = paramsString.split("&");

        for (String pair : pairs) {
            String[] keyAndValue = pair.split("=");
            if (keyAndValue.length == 1)
                paramsMap.put(decodeFromUrlFormat(keyAndValue[0]), DEFAULT_PARAM_VALUE);
            else if (keyAndValue.length == 2)
                paramsMap.put(decodeFromUrlFormat(keyAndValue[0]), decodeFromUrlFormat(keyAndValue[1]));
        }

        return paramsMap;
    }

    /**
     * {@link URLDecoder#decode(String, String)}
     */
    public static String decodeFromUrlFormat(String encoded) {
        try {
            return URLDecoder.decode(encoded, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            throw github.dmitmel.raketaframework.util.exceptions.UnsupportedEncodingException.extractFrom(e);
        }
    }

}
