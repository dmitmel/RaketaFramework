package org.willthisfly.raketaframework;

import java.util.HashMap;

public class URIParser {
    private URIParser() {
        throw new RuntimeException("Can\'t create instance of URIParser");
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
    
    
    public static URI parse(String string) {
        State state = State.PATH;
        
        StringBuilder path = new StringBuilder();
        StringBuilder anchor = new StringBuilder();
        
        StringBuilder paramName = new StringBuilder();
        StringBuilder paramValue = new StringBuilder();
        HashMap<String, String> params = new HashMap<>();
        
    
        for (char c : string.toCharArray()) {
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
        
        // Finishing not finished operations
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
}
