package alexander.ivanov.clazz;

import alexander.ivanov.annotations.*;

public class TestClass {

    @BeforeAll
    static void beforeAll() {

    }

    @AfterAll
    static void afterAll() {

    }

    TestClass() {
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
    void testWithException() {
        throw new RuntimeException("Ошибка при выполнении тестовго метода testWithException()");
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
