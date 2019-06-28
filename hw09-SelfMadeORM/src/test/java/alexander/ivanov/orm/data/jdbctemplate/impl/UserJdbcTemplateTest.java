package alexander.ivanov.orm.data.jdbctemplate.impl;

import alexander.ivanov.orm.data.jdbctemplate.JdbcTemplate;
import alexander.ivanov.orm.data.source.h2.impl.DataDefinitionAndManipulationImpl;
import alexander.ivanov.orm.object.User;
import org.junit.jupiter.api.Test;

class UserJdbcTemplateTest {

    @Test
    void create() {
        JdbcTemplate<User> userJdbcTemplate = new JdbcTemplateImpl<>(new DataDefinitionAndManipulationImpl("jdbc:h2:mem:default4"));
        userJdbcTemplate.create(new User(0L, "qwerty", 11));
    }

    @Test
    void update() {
        JdbcTemplate<User> userJdbcTemplate = new JdbcTemplateImpl<>(new DataDefinitionAndManipulationImpl("jdbc:h2:mem:default3"));
        userJdbcTemplate.create(new User(0L, "qwerty", 11));
        userJdbcTemplate.update(new User(5L, "ytrewq", 88));
        userJdbcTemplate.update(new User(0L, "ytrewq", 88));
    }

    @Test
    void createOrUpdate() {
        JdbcTemplate<User> userJdbcTemplate = new JdbcTemplateImpl<>(new DataDefinitionAndManipulationImpl("jdbc:h2:mem:default2"));
        userJdbcTemplate.createOrUpdate(new User(0L, "qwerty", 11));
    }

    @Test
    void load() {
        JdbcTemplate<User> userJdbcTemplate = new JdbcTemplateImpl<>(new DataDefinitionAndManipulationImpl("jdbc:h2:mem:default1"));
        //userJdbcTemplate.create(new User(555L, "aaa", 11));
        //userJdbcTemplate.createOrUpdate(new User(333L, "bbb", 11));
        userJdbcTemplate.createOrUpdate(new User(123L, "ccc", 11));
        userJdbcTemplate.load(123L, User.class);
        //userJdbcTemplate.load(0, User.class); //error no data found
    }
}