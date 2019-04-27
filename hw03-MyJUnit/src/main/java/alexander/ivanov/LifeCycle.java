package alexander.ivanov;

public abstract class LifeCycle {
    abstract protected void beforeAll();
    abstract protected void beforeEach();
    abstract protected void test();
    abstract protected void afterEach();
    abstract protected void afterAll();
    abstract protected void run();
}
