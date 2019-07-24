package alexander.ivanov.jsonconverter.json;

import alexander.ivanov.jsonconverter.json.util.FieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JsonWrappers {
    private static final Logger logger = LoggerFactory.getLogger(JsonWrappers.class);

    public static class RecordWrapper implements Wrapper<Map<String, Object>> {
        @Override
        public String wrap(Map<String, Object> source) {
            StringBuilder tmp = new StringBuilder();
            Iterator iterator = source.keySet().iterator();
            while (iterator.hasNext()) {
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
            while (iterator.hasNext()) {
                Object value = iterator.next();
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

    public static class ObjectWrapper implements Wrapper<Object> {
        @Override
        public String wrap(Object source) {
            StringBuilder tmp = new StringBuilder();

            Map<String, Object> map = FieldUtil.getFieldsNameValueMap(source);

            Iterator keysIterator = map.keySet().iterator();
            while (keysIterator.hasNext()) {
                String keyName = keysIterator.next().toString();
                tmp.append(String.format("%s: %s",
                        new StringWrapper().wrap(keyName),
                        new ValueWrapper().wrap(map.get(keyName)))
                );
                if (keysIterator.hasNext()) {
                    tmp.append(", ");
                }
            }

            return String.format("{%s}", tmp.toString());
        }
    }


    public static class ValueWrapper implements Wrapper<Object> {
        @Override
        public String wrap(Object source) {
            String res = null;
            printInfo(source);
            if (Objects.isNull(source)) {
                res = new NullWrapper().wrap(source);
            } else if (source.getClass().isArray()) {
                Collection values = IntStream.range(0, Array.getLength(source))
                        .mapToObj(value -> Array.get(source, value))
                        .collect(Collectors.toList());
                res = new ArrayWrapper().wrap(values);
            } else if (source instanceof Collection) {
                res = new ArrayWrapper().wrap((Collection<?>) source);
            } else if (source instanceof Number) {
                res = source.toString();
            } else if (source instanceof String) {
                res = new StringWrapper().wrap(source.toString());
            } else if (source instanceof Character) {
                Character charValue = (char)source;
                //String charString = !Character.isLetterOrDigit(charValue) ? "" : Character.valueOf((char) source).toString();
                String charString = !Character.isLetterOrDigit(charValue) ? "\\u0000" : Character.valueOf((char) source).toString();
                //String charString = Character.valueOf((char) source).toString();
                res = new StringWrapper().wrap(charString);
            } else if (source instanceof Boolean) {
                res = new BooleanWrapper().wrap((boolean) source);
            } else {
                res = new ObjectWrapper().wrap(source);
            }

            return res;
        }

        private void printInfo(Object source) {
            if (Objects.nonNull(source)) {
                logger.info("source = {}" +
                                "\nClass = {}" +
                                "\nTypeName = {}" +
                                "\nName = {}" +
                                "\nisPrimitive = {}" +
                                "\nisArray = {}" +
                                "\n~~~ = {}" +
                                "\n",
                        source,
                        source.getClass(),
                        source.getClass().getTypeName(),
                        source.getClass().getSimpleName(),
                        source.getClass().isPrimitive(),
                        source.getClass().isArray(),
                        null
                );
            }
        }
    }

    private interface Wrapper<T> {
        String wrap(T source);
    }
}
