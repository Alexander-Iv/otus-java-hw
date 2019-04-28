package alexander.ivanov;

import alexander.ivanov.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TestRunner {
    private static Logger log = Logger.getGlobal();
    private static Class<?> clazz;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$s] %5$s %n");
    }

    TestRunner(Class<?> clazz) {
        this.clazz = clazz;
    }

    private void beforeAll() {
        invoke(clazz, getMethods(clazz, BeforeAll.class));
    }

    private void test() {
        Object test = null;
        for (Method m : getMethods(clazz, Test.class)) {
            try {
                test = newInstance(clazz);
                invoke(test, getMethods(test, BeforeEach.class));
                invoke(test, m);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                invoke(test, getMethods(test, AfterEach.class));
            }
        }
    }

    private void afterAll() {
        invoke(clazz, getMethods(clazz, AfterAll.class));
    }

    private void run() {
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

    private static List<Method> getMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<Method> methods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
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

    public static void invoke(Class<?> clazz, List<Method> methods) {
        for (Method m : methods) {
            if (Modifier.isStatic(m.getModifiers())) {
                invoke(clazz, m);
            }
        }
    }
}
