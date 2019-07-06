package alexander.ivanov.orm.data.source.h2.util;

import alexander.ivanov.orm.object.User;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class ReflectionHelperTest {

    @Test
    void exists() {
        String result = ReflectionHelper.objectToSimpleSelect(new User(1L, "qwerty", 1));
        System.out.println("result = " + result);
        assertEquals("SELECT 1 FROM USER WHERE id = ?_0", result);
    }

    @Test
    void selectByObjectFields() {
        String result = ReflectionHelper.selectByObjectFields(new User(1L, "qwerty", 1));
        System.out.println("result = " + result);
        assertEquals("SELECT id, name, age FROM USER WHERE id = ?_0", result);
    }

    @Test
    void updateByObjectFields() {
        String result = ReflectionHelper.updateByObjectFields(new User(1L, "qwerty", 1));
        System.out.println("result = " + result);
        assertEquals("UPDATE USER SET name = ?_1, age = ?_2 WHERE id = ?_0", result);
    }

    @Test
    void insertByObjectFields() {
        String result = ReflectionHelper.insertByObjectFields(new User(1L, "qwerty", 1));
        System.out.println("result = " + result);
        assertEquals("INSERT INTO USER(id, name, age) VALUES(?_0, ?_1, ?_2)", result);
    }
    
    @Test
    void annotatedFields() {
        List fields = ReflectionHelper.getAnnotatedFields(new User(1L, "qwerty", 1));
        System.out.println("fields = " + fields);
        assertEquals(Arrays.asList("id", "name", "age"), fields);
    }

    @Test
    void fieldValues() {
        List values = ReflectionHelper.getFieldValues(new User(1L, "qwerty", 1));
        System.out.println("values = " + values);
        assertEquals(Arrays.asList(1L, "qwerty", 1).toString(), values.toString());
    }
    
    @Test
    void objectToCreate() {
        String result = ReflectionHelper.objectToCreate(new User(1L, "qwerty", 1));
        assertEquals("create table USER \n" +
                "(\tid BIGINT (20) NOT NULL auto_increment, \n" +
                "\tname VARCHAR (255), \n" +
                "\tage INTEGER (3)\n" +
                ");", result);
    }

    @Test
    void objectToSelect() {
        String result = ReflectionHelper.objectToSelect(new User(1L, "qwerty", 1));
        assertEquals("SELECT id, name, age FROM USER WHERE id = ?_0", result);
    }

    @Test
    void objectToInsert() {
        String result = ReflectionHelper.objectToInsert(new User(1L, "qwerty", 1));
        assertEquals("INSERT INTO USER(id, name, age) VALUES(?_0, ?_1, ?_2)", result);
    }

    @Test
    void objectToUpdate() {
        String result = ReflectionHelper.objectToUpdate(new User(1L, "ytrewq", 1));
        assertEquals("UPDATE USER SET name = ?_1, age = ?_2 WHERE id = ?_0", result);
    }
}