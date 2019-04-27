package alexander.ivanov.clazz;

import alexander.ivanov.annotations.AfterEach;
import alexander.ivanov.annotations.BeforeEach;
import alexander.ivanov.annotations.Test;

public class TestBeforeEachException {
    /*@BeforeEach
    public void beforeEach1() {
        throw new RuntimeException();
    }
    @BeforeEach
    public void beforeEach2() {

    }*/
    @BeforeEach
    public void beforeEach1() {

    }
    @BeforeEach
    public void beforeEach2() {
        throw new RuntimeException();
    }

    @Test
    public void test() {

    }

    @AfterEach
    public void afterEach1() {

    }

    @AfterEach
    public void afterEach2() {

    }
}
