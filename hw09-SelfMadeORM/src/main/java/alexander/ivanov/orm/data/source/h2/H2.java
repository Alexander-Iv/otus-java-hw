package alexander.ivanov.orm.data.source.h2;

import java.util.List;
import java.util.Map;

public interface H2 {
    void create(String query);
    void create(String query, List params);

    int insert(String query);
    int insert(String query, List params);

    Map<Object, List<Object>> select(String query);
    Map<Object, List<Object>> select(String query, List params);

    int update(String query);
    int update(String query, List params);

    int delete(String query);
    int delete(String query, List params);
}
