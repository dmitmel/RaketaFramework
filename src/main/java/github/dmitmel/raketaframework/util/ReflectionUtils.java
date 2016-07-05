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
            if (!accessible) method.setAccessible(true);
            result = method.invoke(object, args);
            if (!accessible) method.setAccessible(false);

            return result;
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw github.dmitmel.raketaframework.util.exceptions.InvocationTargetException.extractFrom(e);
        } catch (IllegalAccessException e) {
            // This's unreachable code because java.lang.IllegalAccessException can be thrown only if accessible object
            // is locked
            throw new RuntimeException("unreachable code");
        }
    }
}
