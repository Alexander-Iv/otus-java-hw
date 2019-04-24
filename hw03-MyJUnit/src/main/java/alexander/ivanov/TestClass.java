package alexander.ivanov;

import alexander.ivanov.annotations.*;

public class TestClass {

    @BeforeAll
    static void beforeAll() {

    }

    @AfterAll
    static void afterAll() {

    }

    TestClass() {
        System.out.println("TestClass()");
    }

    @BeforeEach
    void beforeEach3() {

    }

    @BeforeEach
    void beforeEach() {
    }

    @BeforeEach
    void beforeEach2() {

    }

    @Test
    void testOne() {

    }

    @Test
    void testWitchException() {
        throw new RuntimeException();
    }

    @Test
    void testTwo() {

    }

    @AfterEach
    void afterEach2() {

    }

    @AfterEach
    void afterEach() {

    }
}
