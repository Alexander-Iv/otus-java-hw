package alexander.ivanov.clazz;

import alexander.ivanov.annotations.AfterAll;
import alexander.ivanov.annotations.BeforeAll;
import alexander.ivanov.annotations.Test;

public class TestBeforeAllException {

    @BeforeAll
    public void beforeAllWithoutStatic() {
    }

    @BeforeAll
    public static void beforeAll2() {
        throw new RuntimeException();
    }

    @Test
    public void test() {

    }

    @AfterAll
    public static void afterAll() {

    }

    @AfterAll
    public void afterAllWithoutStatic() {
    }
}
