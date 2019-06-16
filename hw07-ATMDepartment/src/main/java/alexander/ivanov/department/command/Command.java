package alexander.ivanov.department.command;

public interface Command {
    <T> T execute();
}
