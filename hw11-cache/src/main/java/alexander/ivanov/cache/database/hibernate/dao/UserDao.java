package alexander.ivanov.cache.database.hibernate.dao;

import alexander.ivanov.cache.database.hibernate.model.User;

import java.util.List;

public interface UserDao extends JdbcTemplate<User> {
    @Override
    void create(User objectData);

    @Override
    void update(User objectData);

    @Override
    <T> T load(long id, Class<T> clazz);

    List<User> loadAll();
}