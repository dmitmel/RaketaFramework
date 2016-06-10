package github.dmitmel.raketaframework.web.handle;

import github.dmitmel.raketaframework.web.HTTPRequest;

import java.util.Map;
import java.util.regex.Matcher;

public class RequestData {
    public final Matcher urlPatternMatcher;
    public final Map<String, String> urlParams;
    public final HTTPRequest httpRequest;

    public RequestData(Matcher urlPatternMatcher, HTTPRequest httpRequest) {
        this.urlPatternMatcher = urlPatternMatcher;
        this.urlParams = httpRequest.url.params;
        this.httpRequest = httpRequest;
    }

    public boolean isForm() {
        return  httpRequest.headers.containsKey("Content-Type") &&
                httpRequest.headers.get("Content-Type").contains("form");
    }

    public String getUrlParam(String key) {
        return urlParams.get(key);
    }
    public String getUrlParamOrElse(String key, String orElse) {
        return urlParams.getOrDefault(key, orElse);
    }

    public String getMatcherGroup(int i) {
        return urlPatternMatcher.group(i + 1);
    }
    public String getMatcherGroupOrElse(int i, String orElse) {
        if (i >= 0 && i + 1 < urlPatternMatcher.groupCount())
            return urlPatternMatcher.group(i);
        else
            return orElse;
    }
}
