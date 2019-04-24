import alexander.ivanov.TestClass;
import alexander.ivanov.TestRunnerI;
import alexander.ivanov.TestRunnerImp;

public class TestTest {
    public static void main(String[] args) {
        TestRunnerI runner = new TestRunnerImp();
        runner.run(TestClass.class);
    }
}