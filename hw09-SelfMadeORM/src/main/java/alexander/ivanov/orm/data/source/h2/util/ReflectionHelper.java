package alexander.ivanov.orm.data.source.h2.util;

import alexander.ivanov.orm.data.source.h2.H2;
import alexander.ivanov.orm.data.source.h2.annotations.Column;
import alexander.ivanov.orm.data.source.h2.annotations.Id;
import alexander.ivanov.orm.data.source.h2.annotations.Size;
import alexander.ivanov.orm.data.source.h2.annotations.Table;
import org.h2.value.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionHelper {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionHelper.class);

    public static String objectToCreate(Object object) {
        //logger.info("object = " + object);
        AnnotationProperties properties = new AnnotationProperties(setAnnotationProperties(object));
        //logger.info("properties = " + properties);

        String tableName = properties.getPropertyByAnnotation(Table.class).getProperties()
                .getOrDefault("name", object.getClass().getSimpleName());

        StringBuilder query = new StringBuilder();
        query.append("create table");
        query.append(" " + tableName + " \n");

        AnnotationProperty id = properties.getPropertyByAnnotation(Id.class);
        List<AnnotationProperty> fields = properties.getPropertiesByAnnotation(Column.class);
        if (!id.getTarget().isEmpty()
                && !fields.stream().anyMatch(annotationProperty -> annotationProperty.getTarget().equals(id.getTarget()))) {
            //logger.info("0 fields = " + fields);
            fields.add(0, id);
            //logger.info("1 fields = " + fields);
        }
        if (!fields.isEmpty()) {
            query.append("(");

            ListIterator iterator = fields.listIterator();
            while (iterator.hasNext()) {
                fields.get(iterator.nextIndex()).getTarget().keySet().forEach(o -> {
                    query.append("\t");
                    query.append(o);
                    query.append(
                            DataType.getTypes()
                                    .stream()
                                    .filter(dataType -> dataType.type == DataType.getTypeFromClass(fields.get(iterator.nextIndex()).getTarget().get(o)))
                                    .map(dataType -> " " + dataType.name)
                                    .findFirst().orElse("")
                    );

                    String size = Optional.ofNullable(properties.getPropertyByAnnotation(Size.class, o).getProperties().get("value")).orElse("");
                    if (!size.isEmpty()) {
                        query.append(" (" + size + ")");
                    }

                    String idProperty = Optional.ofNullable(properties.getPropertyByAnnotation(Id.class, o).getProperties().get("value")).orElse("");
                    if (!idProperty.isEmpty()) {
                        query.append(" " + idProperty);
                    }

                    iterator.next();
                    if (!(iterator.nextIndex() == fields.size())) {
                        query.append(", \n");
                    }
                });
            }
            query.append("\n);");
        }

        logger.info("query:\n" + query);

        return query.toString();
    }

    public static String objectToSelect(Object object) {
        AnnotationProperties properties = new AnnotationProperties(setAnnotationProperties(object));
        StringBuilder conditions = new StringBuilder();

        StringBuilder query = new StringBuilder();
        query.append("SELECT ");

        AnnotationProperty id = properties.getPropertyByAnnotation(Id.class);
        List<AnnotationProperty> fields = properties.getPropertiesByAnnotation(Column.class);
        if (!id.getTarget().isEmpty()
                && !fields.stream().anyMatch(annotationProperty -> annotationProperty.getTarget().equals(id.getTarget()))) {
            fields.add(0, id);
        }
        if (!fields.isEmpty()) {
            ListIterator iterator = fields.listIterator();
            while (iterator.hasNext()) {
                fields.get(iterator.nextIndex()).getTarget().keySet().forEach(o -> {
                    query.append(o);
                    String tmp = getValueByFieldName(object, o);
                    if (!tmp.isEmpty()) {
                        if (iterator.nextIndex() > 0) {
                            conditions.append(" AND ");
                        }
                        conditions.append(o + " = " + tmp);
                    }

                    iterator.next();
                    if (!(iterator.nextIndex() == fields.size())) {
                        query.append(", ");
                    }
                });
            }
        }

        String tableName = properties.getPropertyByAnnotation(Table.class).getProperties()
                .getOrDefault("name", object.getClass().getSimpleName());
        query.append(" FROM " + tableName + " \n");

        if (!conditions.toString().isEmpty()) {
            query.append("WHERE " + conditions + ";\n");
        }

        logger.info("query:\n" + query);
        //logger.info("conditions = " + conditions);

        return query.toString();
    }

    public static String objectToInsert(Object object) {
        AnnotationProperties properties = new AnnotationProperties(setAnnotationProperties(object));
        StringBuilder values = new StringBuilder();

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO");
        String tableName = properties.getPropertyByAnnotation(Table.class).getProperties()
                .getOrDefault("name", object.getClass().getSimpleName());
        query.append(" " + tableName + " ");

        AnnotationProperty id = properties.getPropertyByAnnotation(Id.class);
        List<AnnotationProperty> fields = properties.getPropertiesByAnnotation(Column.class);
        if (!id.getTarget().isEmpty()
                && !fields.stream().anyMatch(annotationProperty -> annotationProperty.getTarget().equals(id.getTarget()))) {
            fields.add(0, id);
        }
        if (!fields.isEmpty()) {
            query.append("(");
            values.append("(");
            ListIterator iterator = fields.listIterator();
            while (iterator.hasNext()) {
                fields.get(iterator.nextIndex()).getTarget().keySet().forEach(o -> {
                    String tmp = getValueByFieldName(object, o);
                    if (!tmp.isEmpty()) {
                        query.append(o);
                        values.append(tmp);
                    }

                    iterator.next();
                    if (!(iterator.nextIndex() == fields.size())) {
                        query.append(", ");
                        values.append(", ");
                    }
                });
            }
            query.append(")");
            values.append(");");
        }

        query.append(" VALUES " + values.toString());

        logger.info("query:\n" + query);
        //logger.info("values = " + values);

        return query.toString();
    }

    public static String objectToUpdate(Object object) {
        AnnotationProperties properties = new AnnotationProperties(setAnnotationProperties(object));
        StringBuilder conditions = new StringBuilder();

        StringBuilder query = new StringBuilder();
        query.append("UPDATE");
        String tableName = properties.getPropertyByAnnotation(Table.class).getProperties()
                .getOrDefault("name", object.getClass().getSimpleName());
        query.append(" " + tableName + " ");

        AnnotationProperty id = properties.getPropertyByAnnotation(Id.class);
        List<AnnotationProperty> fields = properties.getPropertiesByAnnotation(Column.class);
        fields.removeIf(annotationProperty -> annotationProperty.getTarget().equals(id.getTarget()));
        if (!fields.isEmpty()) {
            ListIterator iterator = fields.listIterator();
            while (iterator.hasNext()) {
                fields.get(iterator.nextIndex()).getTarget().keySet().forEach(o -> {
                    String tmp = getValueByFieldName(object, o);
                    if (!tmp.isEmpty()) {
                        conditions.append(o + " = " + tmp);
                    }

                    iterator.next();
                    if (!(iterator.nextIndex() == fields.size())) {
                        conditions.append(", ");
                    }
                });

            }
        }

        if (!conditions.toString().isEmpty()) {
            query.append("SET " + conditions);
        }

        id.getTarget().keySet().forEach(s -> {
            String tmp = getValueByFieldName(object, s);
            if (!tmp.isEmpty()) {
                query.append(" WHERE " + s + " = " + tmp);
            }
        });

        query.append(";\n");

        logger.info("query:\n" + query);
        //logger.info("values = " + values);

        return query.toString();
    }
    
    public static Object classToObject(long id, Class<?> clazz, H2 db) {
        logger.info("id = {}, class = {}", id, clazz);

        Object object = null;
        try {
            object = clazz.getConstructor().newInstance();

            AnnotationProperties properties = new AnnotationProperties(setAnnotationProperties(object));
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
                    db.select(objectToSelect(object)));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error: " + e.getMessage());
        }

        logger.info("object = " + object);
        return null;
    }

    private static void setObjectFieldsFromDbSelectResult(Object object, List<Field> fields, Map<Object, List<Object>> dbResult) {
        for (Object headerFieldFromDb : dbResult.keySet()) {
            //logger.info("headerFieldFromDb = " + headerFieldFromDb);
            fields.stream()
                    .filter(field -> field.getName().toUpperCase().equals(headerFieldFromDb.toString().toUpperCase()))
                    //.peek(field -> logger.info("field = " + field))
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

    private static List<AnnotationProperty> setAnnotationProperties(Object object) {
        //class
        List<AnnotationProperty> properties =
                Arrays.asList(object.getClass().getDeclaredAnnotations())
                        .stream()
                        .map(annotation -> new AnnotationProperty(
                                annotation,
                                Collections.singletonMap(object.getClass().getSimpleName(), object.getClass()), //object=name
                                getAnnotationPropertyByObject(object, annotation)))
                .collect(Collectors.toList());


        //fields
        //logger.info("... before properties = " + properties);
        Arrays.asList(object.getClass().getDeclaredFields())
                .stream()
                .map(field -> {
                    return Arrays.asList(field.getDeclaredAnnotations())
                            .stream()
                            //.peek(annotation -> logger.info("1 field = {}, annotation = {}", field.getName(), annotation))
                            .map(annotation -> new AnnotationProperty(
                                                annotation,
                                                Collections.singletonMap(field.getName(), field.getType()),
                                                getAnnotationPropertyByObject(field, annotation))
                            )
                            //.peek(o -> logger.info("o = " + o))
                            .collect(Collectors.toList());
                })
                .filter(objects -> !objects.isEmpty())
                //.peek(objects -> logger.info("objects = " + objects))
                //.collect(Collectors.toList())
                .reduce(properties, (annotationProperties, annotationProperties2) -> {
                    annotationProperties2.forEach(annotationProperty -> annotationProperties.add(annotationProperty));
                    return annotationProperties;
                });
        //logger.info("... after properties = " + properties);
        return properties;
    }

    private static Map<String, String> getAnnotationPropertyByObject(Object source, Annotation annotation) {
        //logger.info("annotation = " + annotation);
        //logger.info("annotation.getMethods() = " + Arrays.asList(annotation.annotationType().getDeclaredMethods()));

        Map<String, String> kv = new HashMap<>();
        Arrays.asList(annotation.annotationType().getDeclaredMethods())
                .stream()
                //.peek(method -> logger.info("method = " + method))
                .forEach(method -> {
                    try {
                        //logger.info("method.getName() = {}, isField = {}", method.getName(), (source instanceof Field));
                        if (source instanceof Field) {
                            kv.put(method.getName(),
                                    ((Field)source).getDeclaredAnnotation(annotation.annotationType())
                                            .getClass()
                                            .getMethod(method.getName())
                                            .invoke(annotation).toString()
                            );
                        } else {
                            kv.put(method.getName(),
                                    source.getClass()
                                            .getDeclaredAnnotation(annotation.annotationType())
                                            .getClass()
                                            .getMethod(method.getName())
                                            .invoke(annotation).toString()
                            );
                        }
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        logger.error("e.getMessage() = " + e.getMessage());
                        e.printStackTrace();
                    }
                });
        //kv.forEach((s, s2) -> logger.info("k = {}, v = {}", s, s2));

        return kv;
    }

    private static String getValueByFieldName(Object object, String name) {
        String value = "";
        try {
            Field f = object.getClass().getDeclaredField(name);
            //logger.info("f = " + f);
            f.setAccessible(true);
            Object obj = f.get(object);
            if(obj != null) {
                //logger.info("obj = {}, {}", obj, (obj == null));
                value = (obj instanceof String) ? "\'" + obj.toString() + "\'" : obj.toString();
            }
            f.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
