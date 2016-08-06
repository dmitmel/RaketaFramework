package org.willthisfly.raketaframework.util;

import java.lang.annotation.Annotation;

public class AnnotationNotFoundException extends RuntimeException {
    public AnnotationNotFoundException(String what, Class<? extends Annotation> annotation) {
        super(String.format("%s isn\'t annotated using %s", what, annotation.getName()));
    }
}
