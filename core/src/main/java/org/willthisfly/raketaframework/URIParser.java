package org.willthisfly.raketaframework;

import org.willthisfly.raketaframework.util.Strings;

import java.util.HashMap;
import java.util.Map;

/**
 * Parser for URIs. Also, will work with URLs, but won't determine
 * protocol, domain or port.
 *
 * <h3>URI structure</h3>
 *
 * <pre><code>
 * <span style="color: #EE7400">/path</span><span style="color: #3BAB1F">#anchor</span><b>?</b><span style="color: #7AA100">paramName</span><b>=</b><span style="color: #00939F">paramValue</span><b>&amp;</b><span style="color: #7AA100">key</span><b>=</b><span style="color: #00939F">value</span>
 * </code></pre>
 *
 * <h3>Notes about parsing</h3>
 *
 * <table>
 *     <caption>Special cases in params</caption>
 *     <tr><th>Pair</th>                    <th>What will happen</th></tr>
 *     <tr><td><code>key=value</code></td>  <td><code>"key"</code> will be set to <code>"value"</code></td></tr>
 *     <tr><td><code>key=</code></td>       <td><code>"key"</code> will be set to empty string</td></tr>
 *     <tr><td><code>key</code></td>        <td><code>"key"</code> will be set to empty string</td></tr>
 * </table>
 */
public class URIParser {
    private URIParser() {
        throw new UnsupportedOperationException("Can\'t create instance of URIParser");
    }
    
    
    private enum State {
        PATH,
        ANCHOR_START,
        ANCHOR,
        PARAMS_START,
        PARAM_NAME,
        PARAM_VALUE_START,
        PARAM_VALUE,
        PARAMS_SEPARATOR
    }
    
    
    public static URI parse(CharSequence charSequence) {
        State state = State.PATH;
        
        StringBuilder path = new StringBuilder();
        StringBuilder anchor = new StringBuilder();
        
        StringBuilder paramName = new StringBuilder();
        StringBuilder paramValue = new StringBuilder();
        HashMap<String, String> params = new HashMap<>();
        
        
        for (char c : Strings.toIterable(charSequence)) {
            if (c == '#' && state == State.PATH)
                state = State.ANCHOR_START;
            else if (c == '?' && (state == State.PATH || state == State.ANCHOR || state == State.ANCHOR_START))
                state = State.PARAMS_START;
            else if (state == State.ANCHOR_START)
                state = State.ANCHOR;
            else if (state == State.PARAMS_START)
                state = State.PARAM_NAME;
            else if (c == '=' && state == State.PARAM_NAME)
                state = State.PARAM_VALUE_START;
            else if (c == '&' && (state == State.PARAM_VALUE || state == State.PARAM_NAME || state == State.PARAM_VALUE_START))
                state = State.PARAMS_SEPARATOR;
            else if (state == State.PARAM_VALUE_START)
                state = State.PARAM_VALUE;
            else if (state == State.PARAMS_SEPARATOR)
                state = State.PARAM_NAME;
            
            
            switch (state) {
                case PATH:
                    path.append(c);
                    break;
                
                case ANCHOR:
                    anchor.append(c);
                    break;
                
                case PARAM_NAME:
                    paramName.append(c);
                    break;
                
                case PARAM_VALUE:
                    paramValue.append(c);
                    break;
                
                case PARAMS_SEPARATOR:
                    String decodedParamName = URI.decode(paramName.toString());
                    String decodedParamValue = URI.decode(paramValue.toString());
                    params.put(decodedParamName, decodedParamValue);
                    
                    paramName = new StringBuilder();
                    paramValue = new StringBuilder();
                    
                    break;
            }
        }
        
        // Finishing not finished operations, if they were working before end of string
        switch (state) {
            case PARAM_NAME:
            case PARAM_VALUE_START: {
                String decodedParamName = URI.decode(paramName.toString());
                params.put(decodedParamName, URI.EMPTY_PARAM_VALUE);
                break;
            }
            
            case PARAM_VALUE: {
                String decodedParamName = URI.decode(paramName.toString());
                String decodedParamValue = URI.decode(paramValue.toString());
                params.put(decodedParamName, decodedParamValue);
                break;
            }
        }
        
        String decodedPath = URI.decode(path.toString());
        String decodedAnchor = URI.decode(anchor.toString());
        return new URI(decodedPath, decodedAnchor, params);
    }
    
    
    public static Map<String, String> parseURILikeParams(CharSequence charSequence) {
        State state = State.PARAM_NAME;
    
        StringBuilder paramName = new StringBuilder();
        StringBuilder paramValue = new StringBuilder();
        HashMap<String, String> params = new HashMap<>();
        
        
        for (char c : Strings.toIterable(charSequence)) {
            if (c == '=' && state == State.PARAM_NAME)
                state = State.PARAM_VALUE_START;
            else if (c == '&' && (state == State.PARAM_VALUE || state == State.PARAM_NAME || state == State.PARAM_VALUE_START))
                state = State.PARAMS_SEPARATOR;
            else if (state == State.PARAM_VALUE_START)
                state = State.PARAM_VALUE;
            else if (state == State.PARAMS_SEPARATOR)
                state = State.PARAM_NAME;
            
            
            switch (state) {
                case PARAM_NAME:
                    paramName.append(c);
                    break;
                
                case PARAM_VALUE:
                    paramValue.append(c);
                    break;
                
                case PARAMS_SEPARATOR:
                    String decodedParamName = URI.decode(paramName.toString());
                    String decodedParamValue = URI.decode(paramValue.toString());
                    params.put(decodedParamName, decodedParamValue);
                    
                    paramName = new StringBuilder();
                    paramValue = new StringBuilder();
                    
                    break;
            }
        }
        
        // Finishing not finished operations, if they were working before end of string
        switch (state) {
            case PARAM_NAME:
            case PARAM_VALUE_START: {
                String decodedParamName = URI.decode(paramName.toString());
                params.put(decodedParamName, URI.EMPTY_PARAM_VALUE);
                break;
            }
            
            case PARAM_VALUE: {
                String decodedParamName = URI.decode(paramName.toString());
                String decodedParamValue = URI.decode(paramValue.toString());
                params.put(decodedParamName, decodedParamValue);
                break;
            }
        }
    
        return params;
    }
}
