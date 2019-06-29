package alexander.ivanov.orm.data.jdbctemplate.impl;

import alexander.ivanov.orm.data.jdbctemplate.JdbcTemplate;
import alexander.ivanov.orm.data.source.h2.impl.DataDefinitionAndManipulationImpl;
import alexander.ivanov.orm.object.Account;
import org.junit.jupiter.api.Test;

public class AccountJdbcTemplateTest {
    @Test
    void create() {
        JdbcTemplate<Account> userJdbcTemplate = new JdbcTemplateImpl<>(new DataDefinitionAndManipulationImpl("jdbc:h2:mem:default"));
        userJdbcTemplate.create(new Account(0L, "qwerty", 11));
    }

    @Test
    void update() {
        JdbcTemplate<Account> userJdbcTemplate = new JdbcTemplateImpl<>(new DataDefinitionAndManipulationImpl("jdbc:h2:mem:default"));
        userJdbcTemplate.create(new Account(0L, "qwerty", 11));
        userJdbcTemplate.update(new Account(0L, "ytrewq", 11));
    }

    @Test
    void createOrUpdate() {
        JdbcTemplate<Account> userJdbcTemplate = new JdbcTemplateImpl<>(new DataDefinitionAndManipulationImpl("jdbc:h2:mem:default"));
        userJdbcTemplate.createOrUpdate(new Account(0L, "qwerty", 11));
    }

    @Test
    void load() {
        JdbcTemplate<Account> userJdbcTemplate = new JdbcTemplateImpl<>(new DataDefinitionAndManipulationImpl("jdbc:h2:mem:default"));
        userJdbcTemplate.create(new Account(0L, "qqqqqq", 11));
        userJdbcTemplate.create(new Account(1L, "wwwwww", 11));
        userJdbcTemplate.create(new Account(2L, "eeeeee", 11));
        userJdbcTemplate.load(2, Account.class);
    }
}
