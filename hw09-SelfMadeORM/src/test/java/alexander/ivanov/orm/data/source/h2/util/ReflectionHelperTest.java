package alexander.ivanov.orm.data.source.h2.util;

import alexander.ivanov.orm.data.source.h2.H2;
import alexander.ivanov.orm.data.source.h2.impl.H2Impl;
import alexander.ivanov.orm.object.User;
import org.junit.jupiter.api.Test;

class ReflectionHelperTest {
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

    @Test
    void classToObject() {
        H2 db = new H2Impl("jdbc:h2:mem:default");
        db.create(ReflectionHelper.objectToCreate(new User(1L, "qwerty", 1)));
        db.insert(ReflectionHelper.objectToInsert(new User(1L, "qwerty", 1)));
        ReflectionHelper.classToObject(1L, User.class, new H2Impl("jdbc:h2:mem:default"));
    }
}