package github.dmitmel.raketaframework.web.handle;

import github.dmitmel.raketaframework.web.HTTPRequest;
import github.dmitmel.raketaframework.web.URL;

import java.util.Map;
import java.util.regex.Matcher;

public class WebFormData extends RequestData {
    public final Map<String, String> formParams;

    public WebFormData(Matcher urlPatternMatcher, HTTPRequest httpRequest) {
        super(urlPatternMatcher, httpRequest);
        this.formParams = URL.parseUrlParams(URL.decodeFromUrlFormat(new String(httpRequest.body)));
    }

    public static WebFormData castFrom(RequestData requestData) {
        return new WebFormData(requestData.urlPatternMatcher, requestData.httpRequest);
    }

    public String getFormParam(String key) {
        return formParams.get(key);
    }
    public String getFormParamOrElse(String key, String orElse) {
        return formParams.getOrDefault(key, orElse);
    }
}
