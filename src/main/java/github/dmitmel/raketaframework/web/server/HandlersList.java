package github.dmitmel.raketaframework.web.server;

import github.dmitmel.raketaframework.util.AnnotationNotFoundException;
import github.dmitmel.raketaframework.util.InvalidMethodSignatureException;
import github.dmitmel.raketaframework.web.handle.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

public class HandlersList implements Iterable<HandlersList.HandlerData> {
    public static final List<Class<?>> VALID_HANDLER_METHOD_RETURN_TYPES = Arrays.asList(
            byte[].class, String.class, Document.class, StringBuilder.class
    );

    private List<HandlerData> handlers = new ArrayList<>();

    public HandlersList(RequestHandler... handlers) {
        for (RequestHandler handler : handlers)
             this.handlers.add(new HandlerData(handler));
    }

    public void add(RequestHandler first, RequestHandler... others) {
        handlers.add(new HandlerData(first));
        for (RequestHandler other : others)
            handlers.add(new HandlerData(other));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<HandlerData> iterator() {
        return handlers.iterator();
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

            String pattern = urlPatternAnnotation.value();
            for (HandlerData handler : handlers)
                if (handler.urlPattern.pattern().equals(pattern))
                    throw new UnsupportedOperationException("duplicate request handlers");
            return Pattern.compile(pattern);
        }

        private Map<String, Method> extractSupportedMethods(Class<? extends RequestHandler> requestHandlerClass) {
            Map<String, Method> out = new HashMap<>(0);

            for (Method method : requestHandlerClass.getDeclaredMethods()) {
                RequestMethod requestMethodAnnotation = method.getAnnotation(RequestMethod.class);

                if (requestMethodAnnotation != null) {
                    String webMethodName = requestMethodAnnotation.value();

                    if (webMethodName.equals("OPTIONS")) {
                        throw new UnsupportedOperationException(
                                "defining handle method for web method OPTIONS");
                    } else if (isMethodSignatureValid(method)) {

                        if (out.containsKey(webMethodName))
                            throw new UnsupportedOperationException("duplicate web-methods");
                        else
                            out.put(webMethodName, method);

                    } else {
                        throw new InvalidMethodSignatureException("must be " +
                                "MODIFIERS RETURN_TYPE SOME_NAME(RequestData|WebFormData data)");
                    }
                }
            }

            if (out.isEmpty())
                throw new NoHandleMethodsInHandlerException(requestHandlerClass);

            return out;
        }

        private boolean isMethodSignatureValid(Method method) {
            boolean paramsAreValid = Arrays.equals(method.getParameterTypes(), new Class<?>[] {RequestData.class}) ||
                    Arrays.equals(method.getParameterTypes(), new Class<?>[] {WebFormData.class});
            boolean returnTypeIsValid = VALID_HANDLER_METHOD_RETURN_TYPES.contains(method.getReturnType());

            return paramsAreValid && returnTypeIsValid;
        }

        public boolean supportsMethod(String method) {
            return supportedMethods.containsKey(method);
        }
    }
}
