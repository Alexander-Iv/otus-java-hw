package alexander.ivanov.cache.database.hibernate.dao;

import alexander.ivanov.cache.database.hibernate.model.User;

import java.util.List;

public interface UserDao extends JdbcTemplate<User> {
    @Override
    void create(User user);

    @Override
    void update(User user);

    @Override
    User load(long id, Class<User> clazz);

    @Override
    List<User> loadAll();
}