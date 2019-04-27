package alexander.ivanov;

import alexander.ivanov.clazz.*;

public class TestTest {
    public static void main(String[] args) {
        TestRunner.run(TestClass.class);
        TestRunner.run(TestClass2.class);
        TestRunner.run(TestBeforeAllException.class);
        TestRunner.run(TestBeforeEachException.class);
    }
}