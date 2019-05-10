package alexander.ivanov.logging.proxy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class SourceAnalyzerForProxyInvoke<T> {
    private static final Logger logger = LoggerFactory.getLogger(SourceAnalyzerForProxyInvoke.class);

    private Class<? extends Annotation> annotation;

    private T source;
    private Method method;

    public SourceAnalyzerForProxyInvoke(Object source, Class<? extends Annotation> annotation) {
        this.source = (T)source;
        this.annotation = annotation;
    }

    public T getSource() {
        return source;
    }

    public Method getMethod() {
        return method;
    }

    public void analyze() {
        logger.info("SourceAnalyzerForProxyInvoke.analyze");
        logger.info("source = " + source);

        for (Method annotatedMethod: ReflectionHelper.getMethods(source, annotation)) {
            logger.info("annotatedMethod = " + annotatedMethod);
            method = annotatedMethod;
        }

        if (method == null) {
            throw new RuntimeException("Annotated method not found");
        }

        logger.info("method.getParameterCount() = " + method.getParameterCount());
    }

    @Override
    public String toString() {
        return "SourceAnalyzerForProxyInvoke{" +
                "\n annotation=" + annotation +
                ",\n source=" + source +
                ",\n method=" + method +
                "\n}";
    }
}
