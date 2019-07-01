package alexander.ivanov.orm.data.source.h2.util;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotationProperty {
    private Annotation annotation;
    private Map<String, Class> target;
    private Map<String, String> properties;

    public AnnotationProperty() {
        target = new HashMap<>();
        properties = new HashMap<>();
    }

    public AnnotationProperty(Annotation annotation, Map<String, Class> target, Map<String, String> properties) {
        this.annotation = annotation;
        this.target = target;
        this.properties = properties;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Map<String, Class> getTarget() {
        return target;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "AnnotationProperty{" +
                "annotation=" + annotation +
                ", target=" + target +
                ", properties=" + properties +
                '}';
    }
}
