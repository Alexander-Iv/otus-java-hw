package alexander.ivanov.cache.database.hibernate.dao;

public interface JdbcTemplate<T> {
    void create(T objectData);
    void update(T objectData);
    default void createOrUpdate(T objectData) {

    } // опционально.
    <T> T load(long id, Class<T> clazz);
}
