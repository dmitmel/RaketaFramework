package github.dmitmel.raketaframework.util;

import java.lang.reflect.Method;

public class ReflectionUtils {
    public static <T> String getPathToClass(Class<T> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    public static Object invokeMethodInAnyCase(Method method, Object object, Object... args) {
        try {
            Object result;

            boolean accessible = method.isAccessible();
            if (accessible) {
                result = method.invoke(object, args);
            } else {
                method.setAccessible(true);
                result = method.invoke(object, args);
                method.setAccessible(false);
            }

            return result;
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw github.dmitmel.raketaframework.util.exceptions.InvocationTargetException.extractFrom(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("isn\'t reachable in this case");
        }
    }
}
