package alexander.ivanov.examples;

import alexander.ivanov.annotations.Log;
import alexander.ivanov.logging.proxy.LoggingProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleLoggingDemo {
    private static final Logger logger = LoggerFactory.getLogger(SimpleLoggingDemo.class);
        
    public static void main(String[] args) {
        logger.info("SimpleLoggingDemo.main ~~~ begin");
        SimpleLogging logging = (SimpleLogging)LoggingProxy.newInstance(
                new SimpleLoggingImpl(),
                SimpleLogging.class,
                Log.class);
        logging.calculation(6);
        logging.add(1F,1F);
        logging.pow(2,2);
        logging.add(1,1);

        /*logger.info("*****");
        logger.info("*****");
        SimpleLoggingImpl logging2 = (SimpleLoggingImpl) LoggingProxy.newInstance(
                new SimpleLoggingImpl(),
                SimpleLogging.class,
                Log.class);
        logging2.foo(2,2);*/
        logger.info("SimpleLoggingDemo.main ~~~ end");
    }
}
