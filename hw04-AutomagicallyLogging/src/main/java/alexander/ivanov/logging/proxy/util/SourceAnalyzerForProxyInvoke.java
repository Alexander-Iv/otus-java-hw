package alexander.ivanov.logging.proxy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SourceAnalyzerForProxyInvoke<T> {
    private static final Logger logger = LoggerFactory.getLogger(SourceAnalyzerForProxyInvoke.class);

    private Class<? extends Annotation> annotation;

    private T source;
    private List<Method> methods = new ArrayList<>();

    public SourceAnalyzerForProxyInvoke(Object source, Class<? extends Annotation> annotation) {
        this.source = (T)source;
        this.annotation = annotation;
    }

    public T getSource() {
        return source;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void analyze() {
        logger.info("SourceAnalyzerForProxyInvoke.analyze");
        logger.info("source = " + source);

        logger.info("annotated methods");
        for (Method annotatedMethod: ReflectionHelper.getMethods(source, annotation)) {
            logger.info("annotatedMethod = " + annotatedMethod);
            methods.add(annotatedMethod);
        }

        if (methods.isEmpty()) {
            throw new RuntimeException("Annotated methods not found");
        }
    }

    @Override
    public String toString() {
        return "SourceAnalyzerForProxyInvoke{" +
                "annotation=" + annotation +
                ", source=" + source +
                ", methods=" + methods +
                '}';
    }
}
