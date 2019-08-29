package alexander.ivanov.ms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ErrorHandlerHelper {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlerHelper.class);

    public static void printErrorStackTrace(StackTraceElement[] stackTraceElements) {
        List<?> errorStack = Arrays.stream(stackTraceElements).map(stackTraceElement -> stackTraceElement.toString() + "\n").collect(Collectors.toList());
        logger.error("ERROR STACKTRACE:\n{}", errorStack);
    }
}
