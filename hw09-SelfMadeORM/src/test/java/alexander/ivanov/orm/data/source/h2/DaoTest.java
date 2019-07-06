package alexander.ivanov.orm.data.source.h2;

import alexander.ivanov.orm.data.source.h2.impl.DaoImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DaoTest {
    private static final Logger logger = LoggerFactory.getLogger(DaoTest.class);

    @Test
    void createCheck() {
        Dao dao = new DaoImpl("jdbc:h2:mem:default11");
        dao.create("create table user (id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3));");
        int count = dao.select("select * from user;");
        logger.info("count = " + count);
        assertTrue(count == 0);
    }


    @Test
    void selectCheck() {
        Dao dao = new DaoImpl("jdbc:h2:mem:default12");
        dao.create("create table user (id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3));");
        int count = dao.select("select * from user;");
        assertTrue(count == 0);
    }

    @Test
    void insertCheck() {
        Dao dao = new DaoImpl("jdbc:h2:mem:default13");
        dao.create("create table user (id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3));");
        dao.insert("insert into user(name, age) values('aaa', 11);");
        dao.insert("insert into user(name, age) values('bbb', 22);");
        dao.insert("insert into user(name, age) values('ccc', 33);");

        int count = dao.select("select * from user;");
        assertTrue(count == 3);
    }

    @Test
    void updateCheck() {
        Dao dao = new DaoImpl("jdbc:h2:mem:default14");
        dao.create("create table user (id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3));");
        dao.insert("insert into user(id, name, age) values(1, 'aaa', 11);");

        int count = dao.update("update user set name = 'ddd' where id = 1");
        assertTrue(count == 1);
    }

    @Test
    void deleteCheck() {
        Dao dao = new DaoImpl("jdbc:h2:mem:default15");
        dao.create("create table user (id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3));");
        dao.insert("insert into user(name, age) values('aaa', 11);");
        dao.insert("insert into user(name, age) values('bbb', 22);");

        dao.delete("delete user where id = 1;");

        int count = dao.select("select * from user;");
        assertTrue(count == 1);
    }

    @Test
    void selectWithParam() {
        Dao dao = new DaoImpl("jdbc:h2:mem:default16");
        dao.create("create table user (id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3));");
        dao.insert("insert into user(name, age) values('aaa', 11);");
        dao.insert("insert into user(name, age) values('ddd', 33);");
        dao.insert("insert into user(name, age) values('bbb', 22);");

        int count = dao.select("select * from user where id = ? and name = ?;", Arrays.asList(2, "ddd"));
        assertTrue(count == 1);
    }

    @Test
    void updateWithParam() {
        Dao dao = new DaoImpl("jdbc:h2:mem:default17");
        dao.create("create table user (id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3));");
        dao.insert("insert into user(name, age) values('aaa', 11);");
        dao.insert("insert into user(name, age) values('ddd', 33);");

        int count = dao.update("update user set name = 'aaa' where id = ?", Collections.singletonList(2));
        assertTrue(count == 1);
    }
}