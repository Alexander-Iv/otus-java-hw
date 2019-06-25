package alexander.ivanov.orm.data.source.h2;

import alexander.ivanov.orm.data.source.h2.impl.H2Impl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

class H2Test {
    private static final Logger logger = LoggerFactory.getLogger(H2Test.class);

    @Test
    void h2() {
        H2 db = new H2Impl("jdbc:h2:mem:default");
        db.update("drop table user;"); //error
        db.create("create table IF NOT EXISTS user (id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3));");

        db.insert("insert into user(name, age) values('aaa', 11);");
        db.insert("insert into user(name, age) values('bbb', 22);");
        db.insert("insert into user(name, age) values('ccc', 33);");

        db.select("select * from user;");

        db.delete("delete user where id = 1;");

        logger.info("***");
        db.update("update user set name = 'ddd' where id = ?", Arrays.asList(2));
        logger.info("*********");
        db.select("select * from user where id = ? and name = ?;", Arrays.asList(2, "ddd"));
        logger.info("*********");

        logger.info("???");
        db.select("select * from user where id between ? and ?", Arrays.asList(0, 10));
        logger.info("???");

        db.select("select * from user where id = ?;", Arrays.asList(2));

        db.update("drop table user;");
    }
}