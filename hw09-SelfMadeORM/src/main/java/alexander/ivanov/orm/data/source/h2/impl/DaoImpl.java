package alexander.ivanov.orm.data.source.h2.impl;

import alexander.ivanov.orm.data.source.h2.Dao;
import alexander.ivanov.orm.data.source.h2.annotations.Column;
import alexander.ivanov.orm.data.source.h2.annotations.Id;
import alexander.ivanov.orm.data.source.h2.util.AnnotationProperties;
import alexander.ivanov.orm.data.source.h2.util.AnnotationProperty;
import alexander.ivanov.orm.data.source.h2.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

public class DaoImpl implements Dao {
    private static final Logger logger = LoggerFactory.getLogger(DaoImpl.class);
    private static final String WILDCARD_WITH_INDEX_PATTERN = "[?][_][0-9]+";
    private Connection connection;
    private Map<Object, List<Object>> table;
    private Map<Class, String> loadedQueries;
    private Map<Class, List<Object>> loadedIds;

    public DaoImpl(String url) {
        try {
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
            table = new HashMap<>();
            loadedQueries = new HashMap<>();
            loadedIds = new HashMap<>();
        } catch (SQLException e) {
            errorHandler(e);
        }
    }

    @Override
    public int create(String query) {
        return create(query, Collections.emptyList());
    }

    @Override
    public int create(String query, List params) {
        logger.info("DaoImpl.create");
        int count = update(query, params);
        if (count > 0) {
            logger.info("Table successfully created!");
        }
        return count;
    }

    @Override
    public int insert(String query) {
        return insert(query, Collections.emptyList());
    }

    @Override
    public int insert(String query, List params) {
        logger.info("DaoImpl.insert");
        return update(query, params);
    }

    @Override
    public int select(String query) {
        return select(query, Collections.emptyList());
    }

    @Override
    public int select(String query, List params) {
        int count = 0;
        List newParams = getSortParamsByWildcardIndexes(query, params);
        String newQuery = query.replaceAll(WILDCARD_WITH_INDEX_PATTERN, "?");
        try (PreparedStatement pst = this.connection.prepareStatement(newQuery)) {
            setParams(pst, /*params*/newParams);
            logger.info("QUERY:\n" + newQuery);
            try (ResultSet rs = pst.executeQuery()) {
                StringBuilder header = new StringBuilder();
                header.append("\n");
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    header.append(String.format("%10s", rs.getMetaData().getColumnName(i)));
                    Object obj = rs.getMetaData().getColumnName(i);
                    table.put(obj, new ArrayList<>());
                }
                header.append("\n");

                StringBuilder body = new StringBuilder();
                while (rs.next()) {
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        body.append(String.format("%10s", rs.getObject(i)));
                        Object obj = rs.getMetaData().getColumnName(i);
                        table.get(obj).add(rs.getObject(i));
                    }
                    body.append("\n");
                    count++;
                }
                if (!body.toString().isEmpty()) {
                    logger.info("RESULT: " + header.toString() + body.toString());
                }
            }
        } catch (SQLException e) {
            errorHandler(e);
        }
        return count;
    }

    @Override
    public int update(String query) {
        return update(query, Collections.emptyList());
    }

    public int update(String query, List params) {
        int count = 0;
        List newParams = getSortParamsByWildcardIndexes(query, params);
        String newQuery = query.replaceAll(WILDCARD_WITH_INDEX_PATTERN, "?");
        try (PreparedStatement pst = connection.prepareStatement(newQuery)) {
            setParams(pst, newParams);
            count = pst.executeUpdate();
            connection.commit();

            String command = getCommandName(query);
            if(!command.isEmpty()) {
                logger.info("{} {} rows", command, count);
            }
        } catch (SQLException e) {
            errorHandler(e);
            rollback(connection);
        }
        return count;
    }

    @Override
    public int delete(String query) {
        return delete(query, Collections.emptyList());
    }

    @Override
    public int delete(String query, List params) {
        logger.info("DaoImpl.delete");
        return update(query, params);
    }

    private List getSortParamsByWildcardIndexes(String query, List params) {
        List newParams = new ArrayList(params.size());
        logger.info("params = " + params);

        Pattern.compile("([?][_][0-9]+)+").matcher(query).results().forEach(matchResult -> {
            String tmp = matchResult.group();
            //logger.info("tmp = " + tmp);
            int index = Integer.valueOf(tmp.substring(tmp.indexOf("?_")+2));
            //logger.info("index = " + index);
            //logger.info("params = " + params.get(index));
            newParams.add(params.get(index));
        });
        logger.info("newParams = " + newParams);

        return newParams.isEmpty() ? params : newParams;
    }

    private void setParams(PreparedStatement preparedStatement, List params) throws SQLException {
        long count = 0;
        if ((count = preparedStatement.toString().codePoints()
                .filter(value -> value == "?".codePointAt(0))
                .count()) > 0) {
            if (count != params.size()) {
                throw new RuntimeException("Incorrect param count.");
            }
        } else {
            return;
        }

        ListIterator iter = params.listIterator();
        while(iter.hasNext()) {
            Object val = iter.next();
            preparedStatement.setObject(iter.nextIndex(), val);
        }
    }

    private String getCommandName(String query) {
        return Arrays.asList(query.split("[ ]"))
                .stream()
                .filter(s -> Arrays.asList("insert", "update", "delete").contains(s.toLowerCase()))
                .findFirst().orElse("");
    }

    private void errorHandler(SQLException e) {
        logger.error("ERROR in DataBase:\n{}, {}.\nStackTrace:\n{}",
                e.getErrorCode(),
                e.getMessage(),
                Arrays.asList(e.getStackTrace())
                        .stream()
                        .map(stackTraceElement -> stackTraceElement.toString() + "\n")
                        .toString()
        );
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            errorHandler(e);
        }
    }

    @Override
    public <T> T getInstance(long id, Class<T> clazz) {
        T object;

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

            String query = null;
            if (loadedQueries.containsKey(clazz)) {
                query = loadedQueries.get(clazz);
            } else {
                query = ReflectionHelper.objectToSelect(object);
            }

            List<Object> idNameInDb = null;
            if (loadedIds.containsKey(clazz)) {
                idNameInDb = loadedIds.get(clazz);
            } else {
                idNameInDb = ReflectionHelper.getIdValue(object);
            }

            select(query, idNameInDb);
            for (Object headerFieldFromDb : table.keySet()) {
                //logger.info("headerFieldFromDb = " + headerFieldFromDb);
                Arrays.asList(object.getClass().getDeclaredFields()).stream()
                        .filter(field -> field.getName().toUpperCase().equals(headerFieldFromDb.toString().toUpperCase()))
                        //.peek(field -> logger.info("field = " + field))
                        .forEach(field -> {
                            field.setAccessible(true);
                            try {
                                //logger.info("dbResult.get(headerFieldFromDb).get(0) = " + dbResult.get(headerFieldFromDb).get(0));
                                if (table.get(headerFieldFromDb).size() > 0) {
                                    field.set(object, table.get(headerFieldFromDb).get(0));
                                } else {
                                    throw new RuntimeException("No data found");
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            field.setAccessible(false);
                        });
            }

            loadedQueries.put(clazz, query);
            loadedIds.put(clazz, idNameInDb);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error: " + e.getMessage());
        }

        return object;
    }
}
