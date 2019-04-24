package alexander.ivanov;

import alexander.ivanov.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestRunnerImp implements TestRunnerI {

    @Override
    public void run(Class<?> clazz) {
        Object main = newInstance(clazz);

        invoke(clazz, main, clazz.getDeclaredMethods(), BeforeAll.class);
        invoke(clazz, main, clazz.getDeclaredMethods(), BeforeEach.class);
        invoke(clazz, main, clazz.getDeclaredMethods(), Test.class);
        invoke(clazz, main, clazz.getDeclaredMethods(), AfterEach.class);
        invoke(clazz, main, clazz.getDeclaredMethods(), AfterAll.class);
    }

    private Object newInstance(Class<?> clazz) {
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            /* берем конструктор по умолчанию*/
            if (constructor.getParameterCount() == 0) {
                try {
                    return constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return null;
    }

    private void invoke(Class<?> clazz, Object object, Method[] methods, Class<? extends Annotation> annotation) {
        for (Method method : methods) {
            try {
                for (Annotation a: method.getDeclaredAnnotationsByType(annotation)) {
                    System.out.println(String.format("method = %s, annotation = %s",
                            method.getName(), annotation.getSimpleName()));
                    method.invoke(object, new Object[]{});
                    System.out.println(String.format("result = %s", "OK"));
                    System.out.println();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format("result = %s", "nOK"));
                System.out.println();
            }
        }

    }
}
