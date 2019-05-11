package alexander.ivanov.logging.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

public class LoggingInvocationHandler implements InvocationHandler {

    private final Logger logger = LoggerFactory.getLogger(LoggingInvocationHandler.class);

    Object source;
    List<Method> methods;

    public LoggingInvocationHandler(Object interfaceClass, List<Method> methods) {
        this.source = interfaceClass;
        this.methods = methods;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        for (Method annotatedMethod: methods) {
            if (annotatedMethod.getName().equals(method.getName())) {
                StringBuilder params = new StringBuilder();
                params.append("(");
                if (args != null) {
                    int i = 0;
                    for (Object o : args) {
                        params.append(o);
                        if (i != args.length-1) {
                            params.append(", ");
                        }
                        i++;
                    }
                }
                params.append(")");
                logger.info("executed method: " + annotatedMethod.getName() + params);
                return annotatedMethod.invoke(this.source, args);
            }
        }
        return method.invoke(this.source, args);
    }
}
