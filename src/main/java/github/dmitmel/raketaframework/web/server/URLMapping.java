package github.dmitmel.raketaframework.web.server;

import github.dmitmel.raketaframework.util.AnnotationNotFoundException;
import github.dmitmel.raketaframework.web.handle.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

public class URLMapping implements Iterable<URLMapping.HandlerData> {
    private List<HandlerData> urls = new ArrayList<>();

    public URLMapping(RequestHandler... handlers) {
        for (RequestHandler handler : handlers)
             urls.add(new HandlerData(handler));
    }

    public void add(RequestHandler first, RequestHandler... others) {
        urls.add(new HandlerData(first));
        for (RequestHandler other : others)
            urls.add(new HandlerData(other));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<HandlerData> iterator() {
        return urls.iterator();
    }

    class HandlerData {
        /**
         * URL pattern. Can be extracted from {@link RequestHandler} using {@link RequestURLPattern} annotation.
         */
        public final Pattern urlPattern;

        /**
         * Supported web-methods. Can be extracted from {@link RequestHandler}'s methods which are annotated with
         * {@link RequestMethod}.
         */
        public final Map<String, Method> supportedMethods;

        public final RequestHandler requestHandlerInstance;

        public HandlerData(RequestHandler requestHandlerInstance) {
            this.requestHandlerInstance = requestHandlerInstance;
            Class<? extends RequestHandler> requestHandlerClass = requestHandlerInstance.getClass();
            this.urlPattern = extractURLPattern(requestHandlerClass);
            this.supportedMethods = extractSupportedMethods(requestHandlerClass);
        }

        private Pattern extractURLPattern(Class<? extends RequestHandler> requestHandlerClass) {
            RequestURLPattern urlPatternAnnotation = requestHandlerClass.getAnnotation(RequestURLPattern.class);
            if (urlPatternAnnotation == null) {
                String exceptionMessage = String.format("%s isn\'t annotated with %s",
                        requestHandlerClass.getTypeName(), RequestURLPattern.class.getTypeName());
                throw new AnnotationNotFoundException(exceptionMessage);
            }

            return Pattern.compile(urlPatternAnnotation.value());
        }

        private Map<String, Method> extractSupportedMethods(
                Class<? extends RequestHandler> requestHandlerClass) {
            Map<String, Method> out = new HashMap<>(0);

            for (Method method : requestHandlerClass.getDeclaredMethods()) {
                RequestMethod requestMethodAnnotation = method.getAnnotation(RequestMethod.class);
                if (requestMethodAnnotation != null) {
                    String webMethodName = requestMethodAnnotation.value();
                    if (webMethodName.equals("OPTIONS"))
                        throw new UnsupportedOperationException(
                                "defining handle method for web method OPTIONS");
                    else
                        out.put(webMethodName, method);
                }
            }

            if (out.isEmpty())
                throw new NoHandleMethodsInHandlerException(requestHandlerClass);

            return out;
        }

        public boolean supportsMethod(String method) {
            return supportedMethods.containsKey(method);
        }
    }
}
