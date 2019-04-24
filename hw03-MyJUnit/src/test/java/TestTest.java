import alexander.ivanov.TestClass;
import alexander.ivanov.TestClass2;
import alexander.ivanov.TestRunnerI;
import alexander.ivanov.TestRunnerImp;

public class TestTest {
    public static void main(String[] args) {
        TestRunnerI runner = new TestRunnerImp();
        runner.run(TestClass.class);
        runner.run(TestClass2.class);
        new TestRunnerImp().run(TestClass2.class);
    }
}