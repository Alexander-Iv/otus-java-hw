package alexander.ivanov.orm.data.source.h2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationProperties {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationProperties.class);
    private List<AnnotationProperty> properties;

    public AnnotationProperties(List<AnnotationProperty> properties) {
        this.properties = properties;
    }

    public AnnotationProperty getPropertyByAnnotation(Class<? extends Annotation> annotation) {

        return properties.stream()
                //.peek(annotationProperty -> logger.info("anno1 = {}, anno2 = {}", annotationProperty.getAnnotation().annotationType(), annotation))
                .filter(annotationProperty -> annotationProperty.getAnnotation().annotationType().equals(annotation))
                //.peek(annotationProperty -> logger.info("anno1 = {}, anno2 = {}", annotationProperty.getAnnotation().annotationType(), annotation))
                .findFirst()
                .get();
    }

    public AnnotationProperty getPropertyByAnnotation(Class<? extends Annotation> annotation, String name) {
        return properties.stream()
                .filter(annotationProperty -> annotationProperty.getAnnotation().annotationType().equals(annotation))
                //.peek(annotationProperty -> logger.info("annotationProperty.getTarget().containsKey(name) = " + annotationProperty.getTarget().containsKey(name)))
                .filter(annotationProperty -> annotationProperty.getTarget().containsKey(name))
                //.peek(annotationProperty -> logger.info("!!! annotationProperty = " + annotationProperty.getProperties().get("value")))
                .findFirst()
                .orElse(new AnnotationProperty());
    }

    public List<AnnotationProperty> getPropertiesByAnnotation(Class<? extends Annotation> annotation) {
        return properties.stream()
                //.peek(annotationProperty -> logger.info("anno1 = {}, anno2 = {}", annotationProperty.getAnnotation().annotationType(), annotation))
                .filter(annotationProperty -> annotationProperty.getAnnotation().annotationType().equals(annotation))
                //.peek(annotationProperty -> logger.info("annotationProperty = " + annotationProperty))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "AnnotationProperties{" +
                "properties=" + properties +
                '}';
    }
}
