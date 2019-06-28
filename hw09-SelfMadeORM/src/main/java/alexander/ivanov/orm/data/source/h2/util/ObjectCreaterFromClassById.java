package alexander.ivanov.orm.data.source.h2.util;

import alexander.ivanov.orm.data.source.h2.DataDefinitionAndManipulation;
import alexander.ivanov.orm.data.source.h2.annotations.Column;
import alexander.ivanov.orm.data.source.h2.annotations.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ObjectCreaterFromClassById {
    private static final Logger logger = LoggerFactory.getLogger(ObjectCreaterFromClassById.class);

    private long id;
    private Class<?> clazz;
    private Object object;
    private DataDefinitionAndManipulation ddm;

    public ObjectCreaterFromClassById(long id, Class<?> clazz, DataDefinitionAndManipulation ddm) {
        this.id = id;
        this.clazz = clazz;
        this.ddm = ddm;
    }

    public Object getInstance() {
        logger.info("id = {}, class = {}", id, clazz);

        try {
            object = clazz.getConstructor().newInstance();

            AnnotationProperties properties = new AnnotationProperties(ReflectionHelper.setAnnotationProperties(object));
            AnnotationProperty idProperty = properties.getPropertyByAnnotation(Id.class);
            String idName = idProperty.getTarget().keySet()
                    .stream()
                    .findFirst()
                    .orElse("");

            Field idField = object.getClass().getDeclaredField(idName);

            idField.setAccessible(true);
            idField.set(object, id);
            idField.setAccessible(false);

            List<AnnotationProperty> fields = properties.getPropertiesByAnnotation(Column.class);
            if (!idProperty.getTarget().isEmpty()
                    && !fields.stream().anyMatch(annotationProperty -> annotationProperty.getTarget().equals(idProperty.getTarget()))) {
                fields.add(0, idProperty);
            }

            setObjectFieldsFromDbSelectResult(object,
                    Arrays.asList(object.getClass().getDeclaredFields()),
                    ddm.select(ReflectionHelper.objectToSelect(object), ReflectionHelper.getIdValue(object)));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error: " + e.getMessage());
        }

        logger.info("object = " + object);
        return object;
    }

    private static void setObjectFieldsFromDbSelectResult(Object object, List<Field> fields, Map<Object, List<Object>> dbResult) {
        for (Object headerFieldFromDb : dbResult.keySet()) {
            logger.info("headerFieldFromDb = " + headerFieldFromDb);
            fields.stream()
                    .filter(field -> field.getName().toUpperCase().equals(headerFieldFromDb.toString().toUpperCase()))
                    .peek(field -> logger.info("field = " + field))
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            //logger.info("dbResult.get(headerFieldFromDb).get(0) = " + dbResult.get(headerFieldFromDb).get(0));
                            if (dbResult.get(headerFieldFromDb).size() > 0) {
                                field.set(object, dbResult.get(headerFieldFromDb).get(0));
                            } else {
                                throw new RuntimeException("No data found");
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        field.setAccessible(false);
                    });
        }
    }
}
