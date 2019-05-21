package alexander.ivanov.logging.proxy;

import alexander.ivanov.logging.Logging;
import alexander.ivanov.logging.proxy.util.SourceAnalyzerForProxyInvoke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class LoggingProxy {
    private static final Logger logger = LoggerFactory.getLogger(LoggingProxy.class);

    public static Object newInstance(Object source,
                                     Class<? extends Logging> sourceInterface,
                                     Class<? extends Annotation> annotation){
        logger.info("LoggingProxy.newInstance");
        logger.info("source = " + source);
        logger.info("sourceInterface = " + sourceInterface);
        logger.info("annotation = " + annotation);
        logger.info("*****");

        SourceAnalyzerForProxyInvoke analyzer = new SourceAnalyzerForProxyInvoke(source, annotation);
        analyzer.analyze();
        logger.info("analyzer = " + analyzer);
        logger.info("*****");

        InvocationHandler handler = new LoggingInvocationHandler(analyzer.getSource(), analyzer.getMethods());

        logger.info("handler = " + handler);
        logger.info("*****");
        logger.info("LoggingProxy.class.getClassLoader() = " + LoggingProxy.class.getClassLoader());

        return Proxy.newProxyInstance(
                LoggingProxy.class.getClassLoader(),
                new Class<?>[]{sourceInterface},
                handler);
    }
}