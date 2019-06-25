package alexander.ivanov.orm.data.jdbctemplate.impl;

import alexander.ivanov.orm.data.jdbctemplate.JdbcTemplate;
import alexander.ivanov.orm.object.User;
import org.junit.jupiter.api.Test;

class UserJdbcTemplateTest {

    @Test
    void create() {
        JdbcTemplate<Object> userJdbcTemplate = new JdbcTemplateImpl();
        userJdbcTemplate.create(new User(0L, "qwerty", 11));
    }

    @Test
    void update() {
        JdbcTemplate<Object> userJdbcTemplate = new JdbcTemplateImpl();
        userJdbcTemplate.create(new User(0L, "qwerty", 11));
        userJdbcTemplate.update(new User(0L, "ytrewq", 11));
    }

    @Test
    void createOrUpdate() {
        JdbcTemplate<Object> userJdbcTemplate = new JdbcTemplateImpl();
        userJdbcTemplate.createOrUpdate(new User(0L, "qwerty", 11));
    }

    @Test
    void load() {
        JdbcTemplate<Object> userJdbcTemplate = new JdbcTemplateImpl();
        userJdbcTemplate.create(new User(555L, "aaa", 11));
        userJdbcTemplate.create(new User(333L, "bbb", 11));
        userJdbcTemplate.create(new User(123L, "ccc", 11));
        userJdbcTemplate.load(123, User.class);
        //userJdbcTemplate.load(0, User.class); //error no data found
    }
}