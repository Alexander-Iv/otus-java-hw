package alexander.ivanov.jsonconverter.json.util;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class FieldUtil {
    public static <T> Map<String, T> getFieldsNameValueMap(T object) {
        List<Field> fields = Arrays.asList(object.getClass().getDeclaredFields()).stream()
                        .filter(field -> !field.getName().contains("this$"))
                        .collect(Collectors.toList());

        Map<String, T> fieldsMap = new LinkedHashMap<>(fields.size());
        fields.forEach(field -> {
            fieldsMap.put(field.getName(), getValue(field, object));
        });
        return fieldsMap;
    }

    private static <T> T getValue(Field field, T object) {
        T value = null;
        try {
            field.setAccessible(true);
            value = (T) field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            field.setAccessible(false);
        }

        return value;
    }
}
