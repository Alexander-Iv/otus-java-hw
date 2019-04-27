package alexander.ivanov;

import alexander.ivanov.annotations.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TestRunner extends LifeCycle {
    private static Logger log = Logger.getGlobal();
    private static Class<?> clazz;
    private Object main;
    private Object test;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$s] %5$s %n");
    }

    TestRunner(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void beforeAll() {
        main = newInstance(clazz);
        invoke(main, getMethods(main, BeforeAll.class));
    }

    @Override
    protected void beforeEach() {
        invoke(test, getMethods(test, BeforeEach.class));
    }

    @Override
    protected void test() {
        test = newInstance(clazz);
        for (Method m : getMethods(test, Test.class)) {
            try {
                beforeEach();
                invoke(test, m);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                afterEach();
            }
        }
    }

    @Override
    protected void afterEach() {
        invoke(test, getMethods(test, AfterEach.class));
    }

    @Override
    protected void afterAll() {
        invoke(main, getMethods(main, AfterAll.class));
    }

    @Override
    protected void run() {
        try {
            beforeAll();
            test();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            afterAll();
        }
    }

    public static void run(Class<?> clazz) {
        log.info("~~~~~ " + clazz.getSimpleName() + " ~~~~~");
        new TestRunner(clazz).run();
        log.info("~~~~~ END ~~~~~");
    }



    private static Object newInstance(Class<?> clazz) {
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            /* берем конструктор по умолчанию*/
            if (constructor.getParameterCount() == 0) {
                try {
                    constructor.setAccessible(true);
                    return constructor.newInstance();
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                } finally {
                    constructor.setAccessible(false);
                }
            }
        }
        return null;
    }

    private static List<Method> getMethods(Object object, Class<? extends Annotation> annotation) {
        List<Method> methods = new ArrayList<>();
        for (Method method : object.getClass().getDeclaredMethods()) {
            for (Annotation a: method.getDeclaredAnnotationsByType(annotation)) {
                methods.add(method);
            }
        }
        return methods;
    }

    private static void invoke(Object object, Method method) {
        log.info(String.format("%-18s",  method.getName()));
        try {
            method.setAccessible(true);
            method.invoke(object, new Object[]{});
            method.setAccessible(false);
        } catch (ReflectiveOperationException e) {
            method.setAccessible(false);
            log.info(String.format("%-18s, result = %s",  method.getName(), false));
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        log.info(String.format("%-18s, result = %s",  method.getName(), true));
    }

    private static void invoke(Object object, List<Method> methods) {
        for (Method m : methods) {
            invoke(object, m);
        }
    }
}
