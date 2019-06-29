package alexander.ivanov.orm.data.source.h2.util;

import alexander.ivanov.orm.data.source.h2.DataDefinitionAndManipulation;
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
        return selectByObjectFields(object);
    }

    public static String objectToInsert(Object object) {
        return insertByObjectFields(object);
    }

    public static String objectToUpdate(Object object) {
        return updateByObjectFields(object);
    }

    public static List<AnnotationProperty> setAnnotationProperties(Object object) {
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

    public static String selectByObjectFields(Object object) {
        List<String> idColums = getAnnotatedFields(object, Collections.singletonList(Id.class));
        List<String> allColums = getAnnotatedFields(object, Arrays.asList(Id.class, Column.class));

        StringBuilder where = new StringBuilder();
        where.append(" WHERE ");
        where.append(listWithWildcardSeparatedBy(idColums, " AND ", "?", 0L));

        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        query.append(listSeparatedBy(allColums, ", "));
        query.append(" FROM ");
        query.append(new AnnotationProperties(setAnnotationProperties(object)).getPropertyByAnnotation(Table.class).getProperties()
                .getOrDefault("name", object.getClass().getSimpleName()));
        query.append(where.toString());

        logger.info(query.toString());
        return query.toString();
    }

    public static String updateByObjectFields(Object object) {
        List<String> idColums = getAnnotatedFields(object, Collections.singletonList(Id.class));
        List<String> onlyColums = getAnnotatedFields(object, Collections.singletonList(Column.class));

        StringBuilder where = new StringBuilder();
        where.append(" WHERE ");
        where.append(listWithWildcardSeparatedBy(idColums, " AND ", "?", 0L));

        StringBuilder query = new StringBuilder();
        query.append("UPDATE ");
        query.append(new AnnotationProperties(setAnnotationProperties(object)).getPropertyByAnnotation(Table.class).getProperties()
                .getOrDefault("name", object.getClass().getSimpleName()));
        query.append(" SET ");
        query.append(listWithWildcardSeparatedBy(onlyColums, ", ", "?", countWildCard(where.toString())));
        query.append(where.toString());

        logger.info(query.toString());
        return query.toString();
    }

    public static String insertByObjectFields(Object object) {
        List<String> idColums = getAnnotatedFields(object, Collections.singletonList(Id.class));
        List<String> allColums = getAnnotatedFields(object, Arrays.asList(Id.class, Column.class));

        StringBuilder values = new StringBuilder();
        values.append(" VALUES");
        values.append("(");
        values.append(listWildcardOnlySeparatedBy(allColums, ", ", "?", 0L));
        values.append(")");

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(new AnnotationProperties(setAnnotationProperties(object)).getPropertyByAnnotation(Table.class).getProperties()
                .getOrDefault("name", object.getClass().getSimpleName()));
        query.append("(");
        query.append(listSeparatedBy(allColums, ", "));
        query.append(")");
        query.append(values.toString());

        logger.info(query.toString());
        return query.toString();
    }

    private static String listSeparatedBy(List<String> list, String separator) {
        return listWithWildcardSeparatedBy(list, separator, "", 0);
    }

    private static String listWithWildcardSeparatedBy(List<String> list, String separator, String wildcard, long wildCardCount) {
        StringBuilder tmp = new StringBuilder();
        ListIterator li = list.listIterator();
        while (li.hasNext()) {
            tmp.append(li.next());
            if (wildcard.contains("?")) {
                tmp.append(" = ?" + "_" + (wildCardCount + li.previousIndex()));
            }
            if (!(li.nextIndex() == list.size())) {
                tmp.append(separator);
            }
        }
        return tmp.toString();
    }

    private static long countWildCard(String text) {
        return text.chars().mapToObj(Character::toString).filter(s -> s.equals("?")).count();
    }

    private static String listWildcardOnlySeparatedBy(List<String> list, String separator, String wildcard, long wildCardCount) {
        StringBuilder tmp = new StringBuilder();
        ListIterator li = list.listIterator();
        while (li.hasNext()) {
            li.next();
            if (wildcard.contains("?")) {
                tmp.append("?" + "_" + (wildCardCount + li.previousIndex()));
            }
            if (!(li.nextIndex() == list.size())) {
                tmp.append(separator);
            }
        }
        return tmp.toString();
    }


    public static List<String> getAnnotatedFields(Object object) {
        return getAnnotatedFields(object, Arrays.asList(Id.class, Column.class));
    }

    public static List<String> getAnnotatedFields(Object object, List<Class<? extends Annotation>> anotations) {
        AnnotationProperties properties = new AnnotationProperties(setAnnotationProperties(object));

        List<AnnotationProperty> fields = new ArrayList<>();
        anotations.stream()
                .map(properties::getPropertiesByAnnotation)
                //.distinct()
                .reduce(fields, (annotationProperties, annotationProperties2) -> {
                    annotationProperties2.stream()
                            .forEach(annotationProperty -> {
                                if (annotationProperties.stream()
                                        .noneMatch(annotationProperty1 ->
                                                annotationProperty1.getTarget().equals(annotationProperty.getTarget()))) {
                                    annotationProperties.add(annotationProperty);
                                }
                    });
                    return annotationProperties;
                });

        return fields.stream().map(annotationProperty -> annotationProperty.getTarget().keySet())
                .map(Set::iterator)
                .map(Iterator::next)
                .collect(Collectors.toList());
    }

    public static List<Object> getIdValue(Object object) {
        return getFieldValues(object, Arrays.asList(Id.class));
    }

    public static List<Object> getFieldValues(Object object) {
        return getFieldValues(object, Arrays.asList(Id.class, Column.class));
    }

    public static List<Object> getFieldValues(Object object, List<Class<? extends Annotation>> annotations) {
        return Arrays.asList(object.getClass().getDeclaredFields())
                .stream()
                .filter(field -> getAnnotatedFields(object, annotations).stream().anyMatch(s -> s.equals(field.getName())))
                .map(field -> getValueByFieldName(object, field.getName()))
                .collect(Collectors.toList());
    }

    private static Object getValueByFieldName(Object object, String name) {
        Object value = "";
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
