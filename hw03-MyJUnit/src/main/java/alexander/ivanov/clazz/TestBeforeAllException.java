package alexander.ivanov.clazz;

import alexander.ivanov.annotations.AfterAll;
import alexander.ivanov.annotations.BeforeAll;
import alexander.ivanov.annotations.Test;

public class TestBeforeAllException {
    @BeforeAll
    public void beforeAll() {
        throw new RuntimeException();
    }

    @Test
    public void test() {

    }

    @AfterAll
    public void afterAll() {

    }
}
