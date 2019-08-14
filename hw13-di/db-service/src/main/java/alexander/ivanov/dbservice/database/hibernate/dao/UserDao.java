package alexander.ivanov.dbservice.database.hibernate.dao;

import alexander.ivanov.dbservice.database.hibernate.model.User;

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