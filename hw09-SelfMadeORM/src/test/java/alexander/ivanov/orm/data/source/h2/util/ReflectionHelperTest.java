package alexander.ivanov.orm.data.source.h2.util;

import alexander.ivanov.orm.data.source.h2.DataDefinitionAndManipulation;
import alexander.ivanov.orm.data.source.h2.impl.DataDefinitionAndManipulationImpl;
import alexander.ivanov.orm.object.User;
import org.junit.jupiter.api.Test;

import java.util.List;

class ReflectionHelperTest {

    @Test
    void selectByObjectFields() {
        String result = ReflectionHelper.selectByObjectFields(new User(1L, "qwerty", 1));
        System.out.println("result = " + result);
    }

    @Test
    void updateByObjectFields() {
        String result = ReflectionHelper.updateByObjectFields(new User(1L, "qwerty", 1));
        System.out.println("result = " + result);
    }

    @Test
    void insertByObjectFields() {
        String result = ReflectionHelper.insertByObjectFields(new User(1L, "qwerty", 1));
        System.out.println("result = " + result);
    }
    
    @Test
    void annotatedFields() {
        List fields = ReflectionHelper.getAnnotatedFields(new User(1L, "qwerty", 1));
        System.out.println("fields = " + fields);
    }

    @Test
    void fieldValues() {
        List values = ReflectionHelper.getFieldValues(new User(1L, "qwerty", 1));
        System.out.println("values = " + values);
    }
    
    @Test
    void objectToCreate() {
        ReflectionHelper.objectToCreate(new User(1L, "qwerty", 1));
    }

    @Test
    void objectToSelect() {
        ReflectionHelper.objectToSelect(new User(1L, "qwerty", 1));
    }

    @Test
    void objectToInsert() {
        ReflectionHelper.objectToInsert(new User(1L, "qwerty", 1));
    }

    @Test
    void objectToUpdate() {
        ReflectionHelper.objectToUpdate(new User(1L, "ytrewq", 1));
    }
}