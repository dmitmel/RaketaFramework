package org.willthisfly.raketaframework.routes;

import org.willthisfly.raketaframework.RequestParams;
import org.willthisfly.raketaframework.exceptions.InstantiationException;
import org.willthisfly.raketaframework.exceptions.InvocationTargetException;
import org.willthisfly.raketaframework.exceptions.NoSuchMethodException;
import org.willthisfly.raketaframework.util.AnnotationNotFoundException;
import org.willthisfly.raketaframework.util.Reflection;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Router for making <a href="http://flask.pocoo.org/">Flask</a>-like applications.
 *
 * <h3>Example</h3>
 *
 * <pre><code>
 * public class App {
 *     public static void main(String[] args) {
 *         MethodBasedRouter router = new MethodBasedRouter();
 *         router.addMethod(App.class, "helloWorld");
 *         router.addMethod(App.class, "serveFile");
 *         router.addMethod(App.class, "echo");
 *
 *         Server server = new Server(router);
 *         server.start();
 *     }
 *
 *     &#64;AddMethodRoute("/hello")
 *     public static String helloWorld(RequestParams params) {
 *         return "Hello World!";
 *     }
 *
 *     // Named captures can be passed as parameters, if function has more than 1 parameter
 *     &#64;AddMethodRoute("/file/(?&lt;file&gt;.+)")
 *     public static byte[] serveFile(RequestParams params, String file) {
 *         String fullPath = "/" + file;
 *         InputStream inputStream = App.class.getResourceAsStream(fullPath);
 *
 *         if (inputStream == null)
 *             throw new Error404();
 *         else
 *             return Streams.readAllWithClosing(inputStream);
 *     }
 *
 *     &#64;AddMethodRoute(value = "/echo", methods = {"POST"})
 *     public static byte[] echo(RequestParams params) {
 *         HTTPRequest request = params.getRequest();
 *         return request.body;
 *     }
 * }
 * </code></pre>
 */
public class MethodBasedRouter extends Router {
    private final ArrayList<org.willthisfly.raketaframework.routes.Route> routes = new ArrayList<>();
    
    @Override
    public List<org.willthisfly.raketaframework.routes.Route> getRoutes() {
        return routes;
    }
    
    @Override
    public MethodBasedRouter addAll(Router other) {
        this.routes.addAll(other.getRoutes());
        return this;
    }
    
    public MethodBasedRouter addMethod(Class<?> declaringClass, String handlerMethodName) {
        Optional<Method> optionalHandlerMethod =
                Reflection.getMethodByNameInClass(handlerMethodName, declaringClass);
        Method handlerMethod = optionalHandlerMethod.orElseThrow(() ->
                new NoSuchMethodException("unable to find handler method"));
        
        Optional<?> optionalInstance = Reflection.tryToGetInstanceOf(declaringClass);
        Object handlerInstance = optionalInstance.orElseThrow(() ->
                new InstantiationException("unable to get instance of class which declares handler method"));
        
        AddMethodRoute annotation = handlerMethod.getDeclaredAnnotation(AddMethodRoute.class);
        if (annotation == null)
            throw new AnnotationNotFoundException("handler method", AddMethodRoute.class);
        
        Pattern pattern = Pattern.compile(annotation.value());
        String[] methods = annotation.methods();
        
        for (String method : methods)
            routes.add(new MethodBasedRouter.Route(handlerInstance, handlerMethod, pattern, method));
        
        return this;
    }
    
    private class Route implements PatternRoute, MethodRoute {
        private final Object handlerInstance;
        private final Method handlerMethod;
        private final Pattern pattern;
        private final String method;
    
        public Route(Object handlerInstance, Method handlerMethod, Pattern pattern, String method) {
            this.handlerInstance = handlerInstance;
            this.handlerMethod = handlerMethod;
            this.pattern = pattern;
            this.method = method;
        }
    
        @Override
        public String getMethod() {
            return method;
        }
    
        @Override
        public Object handleRequest(RequestParams params) {
            try {
                if (handlerMethod.getParameterCount() == 1) {
                    return Reflection.invokeMethodInAnyCase(handlerMethod, handlerInstance, params);
                } else {
                    Map<String, String> namedCaptures = params.getNamedCaptures();
        
                    ArrayList<Object> args = new ArrayList<>();
                    args.add(params);
                    args.addAll(namedCaptures.values());
                    Object[] argsArray = args.toArray();
        
                    return Reflection.invokeMethodInAnyCase(handlerMethod, handlerInstance, argsArray);
                }
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                
                if (cause instanceof RuntimeException)
                    throw (RuntimeException) cause;
                else
                    throw e;
            }
        }
    
        @Override
        public Object getHandlerClassInstance() {
            return handlerInstance;
        }
    
        @Override
        public Method getHandlerMethod() {
            return handlerMethod;
        }
    
        @Override
        public Pattern getPattern() {
            return pattern;
        }
        
        @Override
        public Route clone() {
            try {
                return (Route) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new UnsupportedOperationException("MethodBasedRouter.Route.clone");
            }
        }
    }
}
