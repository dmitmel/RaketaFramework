package org.willthisfly.raketaframework.routes;

import org.willthisfly.raketaframework.RequestParams;
import org.willthisfly.raketaframework.util.Reflection;

import java.lang.reflect.Method;

public interface MethodRoute extends Route {
    @Override
    default Object handleRequest(RequestParams params) {
        return Reflection.invokeMethodInAnyCase(getHandlerMethod(), getHandlerClassInstance(), params);
    }
    
    Object getHandlerClassInstance();
    Method getHandlerMethod();
}
