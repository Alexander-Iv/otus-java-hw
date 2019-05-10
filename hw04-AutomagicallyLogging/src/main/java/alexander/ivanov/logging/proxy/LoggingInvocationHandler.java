package alexander.ivanov.logging.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LoggingInvocationHandler implements InvocationHandler {

    private final Logger logger = LoggerFactory.getLogger(LoggingInvocationHandler.class);

    Object source;
    Method method;

    public LoggingInvocationHandler(Object interfaceClass, Method method/*, Object[] args*/) {
        this.source = interfaceClass;
        this.method = method;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("******");
        logger.info("executed method: " + this.method.getName() + ", " + args[0]);

        return this.method.invoke(this.source, args);
    }
}
