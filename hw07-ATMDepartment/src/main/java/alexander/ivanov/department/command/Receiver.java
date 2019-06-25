package alexander.ivanov.department.command;

public interface Receiver {
    <T> T execute();
}
