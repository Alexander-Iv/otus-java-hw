package alexander.ivanov;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReflectionHelper {
    private static Logger log = Logger.getGlobal();

    private ReflectionHelper() { }

    public static Object newInstance(Class<?> clazz) {
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

    public static List<Method> getMethods(Object object, Class<? extends Annotation> annotation) {
        List<Method> methods = new ArrayList<>();
        for (Method method : object.getClass().getDeclaredMethods()) {
            for (Annotation a: method.getDeclaredAnnotationsByType(annotation)) {
                methods.add(method);
            }
        }
        return methods;
    }

    public static List<Method> getMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<Method> methods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            for (Annotation a: method.getDeclaredAnnotationsByType(annotation)) {
                methods.add(method);
            }
        }
        return methods;
    }

    public static void invoke(Object object, Method method) {
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

    public static void invoke(Object object, List<Method> methods) {
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
