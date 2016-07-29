package org.willthisfly.raketaframework.routes;

import org.willthisfly.raketaframework.app.RequestParams;
import org.willthisfly.raketaframework.exceptions.InstantiationException;
import org.willthisfly.raketaframework.exceptions.NoSuchMethodException;
import org.willthisfly.raketaframework.util.AnnotationNotFoundException;
import org.willthisfly.raketaframework.util.Reflection;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

public class MethodBasedRouter extends Router {
    private final ArrayList<org.willthisfly.raketaframework.routes.Route> routes = new ArrayList<>();
    
    @Override
    public List<org.willthisfly.raketaframework.routes.Route> getRoutes() {
        return routes;
    }
    
    public void addAll(Router other) {
        this.routes.addAll(other.getRoutes());
    }
    
    public void addRoute(Class<?> declaringClass, String handlerMethodName) {
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
            if (handlerMethod.getParameterCount() == 1) {
                return Reflection.invokeMethodInAnyCase(handlerMethod, handlerInstance, params);
            } else {
                Map<String, String> namedGroups = params.getNamedCaptures();
                String[] namedGroupsValues = namedGroups.values().toArray(new String[0]);
                
                Object[] methodInvokeArgs = new Object[1 + namedGroupsValues.length];
                methodInvokeArgs[0] = params;
                // Detailed method signature:
                // public static void arraycopy(Object[] input, int inputOffset, Object[] dest, int destOffset, int inputLength)
                System.arraycopy(namedGroupsValues, 0, methodInvokeArgs, 1, namedGroupsValues.length);
                
                return Reflection.invokeMethodInAnyCase(handlerMethod, handlerInstance, methodInvokeArgs);
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
    }
}
