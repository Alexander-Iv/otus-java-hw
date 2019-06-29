package alexander.ivanov.orm.data.source.h2.impl;

import alexander.ivanov.orm.data.source.h2.DataDefinitionAndManipulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

public class DataDefinitionAndManipulationImpl implements DataDefinitionAndManipulation {
    private static final Logger logger = LoggerFactory.getLogger(DataDefinitionAndManipulationImpl.class);
    private static final String WILDCARD_WITH_INDEX_PATTERN = "[?][_][0-9]+";
    private Connection connection;

    public DataDefinitionAndManipulationImpl(String url) {
        try {
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            errorHandler(e);
        }
    }

    @Override
    public void create(String query) {
        create(query, Collections.emptyList());
    }

    @Override
    public void create(String query, List params) {
        logger.info("DataDefinitionAndManipulationImpl.create");
        update(query, params);
        logger.info("Table successfully created!");
    }

    @Override
    public int insert(String query) {
        return insert(query, Collections.emptyList());
    }

    @Override
    public int insert(String query, List params) {
        logger.info("DataDefinitionAndManipulationImpl.insert");
        return update(query, params);
    }

    @Override
    public Map<Object, List<Object>> select(String query) {
        return select(query, Collections.emptyList());
    }

    @Override
    public Map<Object, List<Object>> select(String query, List params) {
        Map<Object, List<Object>> table = new HashMap<>();
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
                }
                if (!body.toString().isEmpty()) {
                    logger.info("RESULT: " + header.toString() + body.toString());
                }
            }
        } catch (SQLException e) {
            errorHandler(e);
        }
        return table;
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
        logger.info("DataDefinitionAndManipulationImpl.delete");
        return update(query, params);
    }

    private List getSortParamsByWildcardIndexes(String query, List params) {
        List newParams = new ArrayList(params.size());
        logger.info("params = " + params);

        Pattern.compile("([?][_][0-9]+)+").matcher(query).results().forEach(matchResult -> {
            String tmp = matchResult.group();
            logger.info("tmp = " + tmp);
            int index = Integer.valueOf(tmp.substring(tmp.indexOf("?_")+2));
            logger.info("index = " + index);
            logger.info("params = " + params.get(index));
            newParams.add(params.get(index));
        });
        logger.info("newParams = " + newParams);

        return newParams;
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

}
