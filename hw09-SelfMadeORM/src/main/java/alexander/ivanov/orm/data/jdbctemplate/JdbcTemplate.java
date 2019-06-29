package alexander.ivanov.orm.data.jdbctemplate;

public interface JdbcTemplate<T> {
    void create(T objectData);
    void update(T objectData);
    void createOrUpdate(T objectData); // опционально.
    <T> T load(long id, Class<T> clazz);
}
