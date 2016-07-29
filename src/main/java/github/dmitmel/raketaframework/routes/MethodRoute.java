package github.dmitmel.raketaframework.routes;

import github.dmitmel.raketaframework.app.RequestParams;
import github.dmitmel.raketaframework.util.Reflection;

import java.lang.reflect.Method;

public interface MethodRoute extends Route {
    @Override
    default Object handleRequest(RequestParams params) {
        return Reflection.invokeMethodInAnyCase(getHandlerMethod(), getHandlerClassInstance(), params);
    }
    
    Object getHandlerClassInstance();
    Method getHandlerMethod();
}
