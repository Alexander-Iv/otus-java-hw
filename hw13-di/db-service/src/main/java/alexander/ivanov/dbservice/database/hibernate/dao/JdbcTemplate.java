package alexander.ivanov.dbservice.database.hibernate.dao;

import java.util.Collection;

public interface JdbcTemplate<T> {
    void create(T objectData);
    void update(T objectData);
    default void createOrUpdate(T objectData) {

    } // опционально.
    T load(long id, Class<T> clazz);
    Collection<? extends T> loadAll();
}
