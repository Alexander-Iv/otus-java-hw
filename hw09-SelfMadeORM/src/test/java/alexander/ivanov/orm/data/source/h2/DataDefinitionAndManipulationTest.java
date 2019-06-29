package alexander.ivanov.orm.data.source.h2;

import alexander.ivanov.orm.data.source.h2.impl.DataDefinitionAndManipulationImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

class DataDefinitionAndManipulationTest {
    private static final Logger logger = LoggerFactory.getLogger(DataDefinitionAndManipulationTest.class);

    @Test
    void dataDefinitionAndManipulationTest() {
        DataDefinitionAndManipulation ddm = new DataDefinitionAndManipulationImpl("jdbc:h2:mem:default");
        ddm.update("drop table user;"); //error
        ddm.create("create table IF NOT EXISTS user (id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3));");

        ddm.insert("insert into user(name, age) values('aaa', 11);");
        ddm.insert("insert into user(name, age) values('bbb', 22);");
        ddm.insert("insert into user(name, age) values('ccc', 33);");

        ddm.select("select * from user;");

        ddm.delete("delete user where id = 1;");

        logger.info("***");
        ddm.update("update user set name = 'ddd' where id = ?", Collections.singletonList(2));
        logger.info("*********");
        ddm.select("select * from user where id = ? and name = ?;", Arrays.asList(2, "ddd"));
        logger.info("*********");

        logger.info("???");
        ddm.select("select * from user where id between ? and ?", Arrays.asList(0, 10));
        logger.info("???");

        ddm.select("select * from user where id = ?;", Collections.singletonList(2));

        ddm.update("drop table user;");
    }
}