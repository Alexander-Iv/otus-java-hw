package alexander.ivanov.webserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class CollectionTransformer {
    private static final Logger logger = LoggerFactory.getLogger(CollectionTransformer.class);

    public static StringBuffer toHtmlTable(Collection<?> collection) {
        StringBuffer content = null;
        if (!collection.isEmpty()) {
            content = new StringBuffer();
            Object object = collection.stream().findFirst().get();
            if (object != null) {
                appendFields(content, object.getClass().getDeclaredFields());
                appendRows(content, collection);
            }
        }
        return content;
    }

    private static void appendFields(StringBuffer content, Field[] fields) {
        Arrays.asList(fields).forEach(field -> {
            content.append("<th>").append(camelCaseToHuman(field.getName())).append("</th>");
        });
    }

    private static String camelCaseToHuman(String text) {
        String newText = text.chars()
                .mapToObj(value -> String.valueOf((char)value))
                .reduce((s, s2) -> s += s2.matches("[A-Z]") ? " " + s2 : s2)
                .orElse("");

        String result = newText.substring(0,1).toUpperCase() + newText.substring(1);
        return result;
    }

    private static void appendRows(StringBuffer content, Collection<?> collection) {
        collection.forEach(user -> {
            content.append("<tr>");
            appendFieldValues(content, user, user.getClass().getDeclaredFields());
            content.append("</tr>");
        });
    }

    private static void appendFieldValues(StringBuffer content, Object object, Field[] fields) {
        Arrays.asList(fields).forEach(field -> {
            try {
                field.setAccessible(true);
                String value = field.get(object).toString();
                value = field.getName().toLowerCase().contains("password") ? value.replaceAll(".","*") : value;
                content.append("<td>").append(value).append("</td>");
            } catch (IllegalAccessException e) {
                field.setAccessible(false);
                e.printStackTrace();
            }
        });
    }
}
