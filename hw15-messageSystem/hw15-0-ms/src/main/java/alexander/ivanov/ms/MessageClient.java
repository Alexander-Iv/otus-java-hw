package alexander.ivanov.ms;

public interface MessageClient {
    void init();
    void accept(Message msg);
    default String getName() {
        return this.getClass().getSimpleName();
    }
}
