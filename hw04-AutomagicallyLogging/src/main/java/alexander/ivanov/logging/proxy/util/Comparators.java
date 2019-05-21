package alexander.ivanov.logging.proxy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Comparator;

public class Comparators {
    private static final Logger logger = LoggerFactory.getLogger(Comparators.class);

    public static boolean equals(Method method1, Method method2) {
        Comparator methodComporator = new MethodComparator();
        return methodComporator.compare(method1, method2) == 0 ? true : false;
    }
    
    private static class MethodComparator implements Comparator<Method> {
        @Override
        public int compare(Method o1, Method o2) {
            /*logger.info("o1.getName() = " + o1.getName());
            logger.info("o1.getName() = " + o2.getName());
            logger.info("o1.getReturnType() = " + o1.getReturnType());
            logger.info("o2.getReturnType() = " + o2.getReturnType());
            logger.info(Arrays.asList(o1.getParameters()) + " ==? " + Arrays.asList(o2.getParameters())
                    + " = " + MethodParametersComparator.isEqual(o1.getParameters(), o2.getParameters()));
            logger.info("**********");*/
            
            return (o1 == null || o2 == null ? false : true)
                    && o1.getName().equals(o2.getName())
                    && o1.getReturnType().equals(o2.getReturnType())
                    && MethodParametersComparator.isEqual(o1.getParameters(), o2.getParameters())
                    ? 0 : 1;
        }
    }

    private static class MethodParametersComparator implements Comparator<Parameter[]> {
        @Override
        public int compare(Parameter[] o1, Parameter[] o2) {
            MethodParameterComparator parameterComparator = new MethodParameterComparator();
            return Arrays.compare(o1, o2, parameterComparator);
        }

        public static boolean isEqual(Parameter[] o1, Parameter[] o2) {
            MethodParametersComparator parametersComparator = new MethodParametersComparator();
            return parametersComparator.compare(o1,o2) == 0 ? true : false;
        }
    }

    private static class MethodParameterComparator implements Comparator<Parameter> {
        @Override
        public int compare(Parameter o1, Parameter o2) {
            /*logger.info("o1 = " + o1);
            logger.info("o2 = " + o2);
            logger.info("o1.getName() = " + o1.getName());
            logger.info("o2.getName() = " + o2.getName());
            logger.info("o1.getType() = " + o1.getType());
            logger.info("o2.getType() = " + o2.getType());
            logger.info("o1.equals(o2) = " + o1.equals(o2));
            logger.info("(o1 == null || o2 == null ? true : false) = " + (o1 == null || o2 == null ? true : false));*/

            return (o1 == null || o2 == null ? false : true)
                    && o1.getName().equals(o2.getName())
                    && o1.getType().equals(o2.getType())
                    ? 0 : 1;
        }
    }
}
