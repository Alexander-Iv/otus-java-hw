package alexander.ivanov.hibernate.dao;

import alexander.ivanov.hibernate.model.User;

public interface UserDao extends JdbcTemplate<User> {
    @Override
    void create(User objectData);

    @Override
    void update(User objectData);

    @Override
    <T> T load(long id, Class<T> clazz);
}
