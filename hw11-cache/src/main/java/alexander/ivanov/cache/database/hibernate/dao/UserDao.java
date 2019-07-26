package alexander.ivanov.cache.database.hibernate.dao;

import java.util.List;

public interface UserDao extends JdbcTemplate<User> {
    @Override
    void create(User objectData);

    @Override
    void update(User objectData);

    @Override
    <T> T load(long id, Class<T> clazz);

    User load(String name, String password);

    List<User> loadAll();
}