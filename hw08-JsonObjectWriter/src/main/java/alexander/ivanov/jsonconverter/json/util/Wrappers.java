package alexander.ivanov.jsonconverter.json.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Wrappers {
    private static final Logger logger = LoggerFactory.getLogger(Wrappers.class);

    public static class RecordWrapper implements Wrapper<Map<String, Object>> {
        @Override
        public String wrap(Map<String, Object> source) {
            StringBuilder tmp = new StringBuilder();
            Iterator iterator = source.keySet().iterator();
            while(iterator.hasNext()) {
                Object key = iterator.next();
                Object value = source.get(key);
                tmp.append(" ");
                tmp.append(new StringWrapper().wrap(String.valueOf(key)));
                tmp.append(": ");
                tmp.append(new ValueWrapper().wrap(value));
                if (iterator.hasNext()) {
                    tmp.append(",");
                }
            }
            return String.format("{%s}", Optional.of(tmp.toString()).orElse(""));
        }
    }

    public static class StringWrapper implements Wrapper<String> {
        @Override
        public String wrap(String source) {
            return "\"" + source + "\"";
        }
    }

    public static class ArrayWrapper implements Wrapper<Collection<?>> {
        @Override
        public String wrap(Collection<?> source) {
            StringBuilder tmp = new StringBuilder();
            Iterator iterator = source.iterator();
            while(iterator.hasNext()) {
                Object value = iterator.next();
                //tmp.append(value);
                tmp.append(new ValueWrapper().wrap(value));
                if (iterator.hasNext()) {
                    tmp.append(",");
                }
            }
            return String.format("[%s]", Optional.of(tmp.toString()).orElse(""));
        }
    }

    public static class BooleanWrapper implements Wrapper<Boolean> {
        @Override
        public String wrap(Boolean source) {
            return source.toString();
        }
    }

    public static class NullWrapper implements Wrapper<Object> {
        @Override
        public String wrap(Object source) {
            return source == null ? "null" : "";
        }
    }


    public static class ValueWrapper implements Wrapper<Object> {
        @Override
        public String wrap(Object source) {
            String res = null;
            if(source == null) {
                res = new NullWrapper().wrap(source);
            } else if (source instanceof Number) {
                res = source.toString();
            } else if (source instanceof String) {
                res = new StringWrapper().wrap(source.toString());
            } else if (source instanceof Character) {
                String charString = Character.valueOf((char)source).toString();
                res = new StringWrapper().wrap(charString);
            } else if (source instanceof Collection) {
                res = new ArrayWrapper().wrap((Collection<?>) source);
            } else if (source.getClass().getComponentType() != null) {
                Collection values = IntStream.range(0, Array.getLength(source))
                        .mapToObj(value -> Array.get(source, value))
                        .collect(Collectors.toList());
                res = new ArrayWrapper().wrap(values);
            } else if (source instanceof Boolean){
                res = new BooleanWrapper().wrap((boolean)source);
            } else {
                List<Field> fields = Arrays.asList(source.getClass().getDeclaredFields());

                res =
                fields.stream().map(field -> {
                    field.setAccessible(true);
                    Object value = null;
                    try {
                        value = field.get(source);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    logger.info("{} {} = {};", field.getType(), field.getName(), value);
                    return String.format("%s: %s",
                            new StringWrapper().wrap(field.getName()),
                            new ValueWrapper().wrap(value));
                }).reduce((s, s2) -> s += ", " + s2).orElse("");

                res = String.format("{%s}", res);

                logger.info("res = " + res);
            }

            return res;
        }
    }

    private interface Wrapper<T> {
        String wrap(T source);
    }
}
