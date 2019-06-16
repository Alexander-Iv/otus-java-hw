package alexander.ivanov.department.command;

public interface Invoker {
    <T> T execute(Command command);
}
