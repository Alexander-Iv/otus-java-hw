package alexander.ivanov.orm.data.source.h2.impl;

import alexander.ivanov.orm.data.source.h2.H2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class H2Impl implements H2 {
    private static final Logger logger = LoggerFactory.getLogger(H2Impl.class);
    private Connection connection;

    public H2Impl(String url) {
        try {
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            errorHandler(e);
        }
    }

    @Override
    public void create(String query) {
        create(query, Arrays.asList());
    }

    @Override
    public void create(String query, List params) {
        logger.info("H2Impl.create");
        update(query, params);
        logger.info("Table successfully created!");
    }

    @Override
    public int insert(String query) {
        return insert(query, Arrays.asList());
    }

    @Override
    public int insert(String query, List params) {
        logger.info("H2Impl.insert");
        return update(query, params);
    }

    @Override
    public Map<Object, List<Object>> select(String query) {
        return select(query, Arrays.asList());
    }

    @Override
    public Map<Object, List<Object>> select(String query, List params) {
        Map<Object, List<Object>> table = new HashMap<>();
        try (PreparedStatement pst = this.connection.prepareStatement(query)) {
            setParams(pst, params);
            logger.info("QUERY:\n" + query);
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
        return update(query, Arrays.asList());
    }

    public int update(String query, List params) {
        int count = 0;
        try (PreparedStatement pst = connection.prepareStatement(query)) {

            setParams(pst, params);
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
        return delete(query, Arrays.asList());
    }

    @Override
    public int delete(String query, List params) {
        logger.info("H2Impl.delete");
        return update(query, params);
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
            /*logger.info("val instanceof Integer = " + (val instanceof Integer));
            logger.info("val instanceof Long = " + (val instanceof Long));
            logger.info("val instanceof Double = " + (val instanceof Double));
            logger.info("val instanceof Float = " + (val instanceof Float));*/
            if (val instanceof Integer) {
                preparedStatement.setInt(iter.nextIndex(), (Integer) val);
            } else if (val instanceof Long) {
                preparedStatement.setLong(iter.nextIndex(), (Long) val);
            } else if (val instanceof Double) {
                preparedStatement.setDouble(iter.nextIndex(), (Double) val);
            } else if (val instanceof Float) {
                preparedStatement.setFloat(iter.nextIndex(), (Float) val);
            } else if (val instanceof String) {
                if(!((String) val).startsWith("'")) {
                    val = "'" + val;
                }
                if(!((String) val).endsWith("'")) {
                    val = val + "'";
                }
                logger.info("val = " + val);
                preparedStatement.setString(iter.nextIndex(), (String) val);
            }
            else {
                throw new UnsupportedOperationException("Unsupported parameter type.");
            }
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
