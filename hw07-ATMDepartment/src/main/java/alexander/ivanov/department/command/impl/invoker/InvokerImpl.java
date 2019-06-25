package alexander.ivanov.department.command.impl.invoker;

import alexander.ivanov.department.command.Command;
import alexander.ivanov.department.command.Invoker;

public class InvokerImpl implements Invoker {
    @Override
    public <T> T execute(Command command) {
        return command.execute();
    }
}
