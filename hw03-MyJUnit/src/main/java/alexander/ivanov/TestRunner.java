package alexander.ivanov;

import alexander.ivanov.annotations.*;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public class TestRunner {
    private static Logger log = Logger.getGlobal();
    private Class<?> clazz;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$s] %5$s %n");
    }

    TestRunner(Class<?> clazz) {
        this.clazz = clazz;
    }

    private void beforeAll() {
        ReflectionHelper.invoke(clazz, ReflectionHelper.getMethods(clazz, BeforeAll.class));
    }

    private void test() {
        Object test = null;
        for (Method m : ReflectionHelper.getMethods(clazz, Test.class)) {
            try {
                test = ReflectionHelper.newInstance(clazz);
                ReflectionHelper.invoke(test, ReflectionHelper.getMethods(test, BeforeEach.class));
                ReflectionHelper.invoke(test, m);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ReflectionHelper.invoke(test, ReflectionHelper.getMethods(test, AfterEach.class));
            }
        }
    }

    private void afterAll() {
        ReflectionHelper.invoke(clazz, ReflectionHelper.getMethods(clazz, AfterAll.class));
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
}
