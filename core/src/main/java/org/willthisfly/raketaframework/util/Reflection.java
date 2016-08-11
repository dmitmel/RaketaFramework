package org.willthisfly.raketaframework.util;

import org.willthisfly.raketaframework.exceptions.InstantiationException;
import org.willthisfly.raketaframework.exceptions.InvocationTargetException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class Reflection {
    private Reflection() {
        throw new UnsupportedOperationException("Can\'t create instance of Reflection");
    }
    
    
    public static <T> String getClassDirectory(Class<T> clazz) {
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
            throw InvocationTargetException.extractFrom(e);
        } catch (IllegalAccessException e) {
            // This's unreachable code because java.lang.IllegalAccessException can be thrown only if accessible object
            // is locked
            throw new RuntimeException("unreachable code", e);
        }
    }
    
    public static <T> T invokeConstructorInAnyCase(Constructor<T> constructor, Object... args) {
        try {
            T result;
    
            boolean accessible = constructor.isAccessible();
            if (!accessible) constructor.setAccessible(true);
            result = constructor.newInstance(args);
            if (!accessible) constructor.setAccessible(false);
    
            return result;
        } catch (java.lang.InstantiationException e) {
            throw InstantiationException.extractFrom(e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw InvocationTargetException.extractFrom(e);
        } catch (IllegalAccessException e) {
            // This's unreachable code because java.lang.IllegalAccessException can be thrown only if accessible object
            // is locked
            throw new RuntimeException("unreachable code", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> tryToGetInstanceOf(Class<? extends T> clazz) {
        try {
            Constructor<T>[] constructors = (Constructor<T>[]) clazz.getConstructors();
    
            Optional<Constructor<T>> suitableConstructor = Arrays.stream(constructors)
                    .filter(constructor -> constructor.getParameterCount() == 0)
                    .findFirst();
            return suitableConstructor.map(constructor -> invokeConstructorInAnyCase(constructor));
        } catch (InstantiationException |
                InvocationTargetException e) {
            return Optional.empty();
        }
    }
    
    public static Optional<Method> getMethodByNameInClass(String name, Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst();
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValueInAnyCase(Field field, Object object) {
        try {
            T result;
            
            boolean accessible = field.isAccessible();
            if (!accessible) field.setAccessible(true);
            result = (T) field.get(object);
            if (!accessible) field.setAccessible(false);
            
            return result;
        } catch (IllegalAccessException e) {
            // This's unreachable code because java.lang.IllegalAccessException can be thrown only if accessible object
            // is locked
            throw new RuntimeException("unreachable code", e);
        }
    }
}
