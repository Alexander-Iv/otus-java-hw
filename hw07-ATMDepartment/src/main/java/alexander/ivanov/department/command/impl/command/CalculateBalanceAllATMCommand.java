package alexander.ivanov.department.command.impl.command;

import alexander.ivanov.department.command.Command;
import alexander.ivanov.department.command.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculateBalanceAllATMCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CalculateBalanceAllATMCommand.class);
    private Receiver receiver;

    public CalculateBalanceAllATMCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public Number execute() {
        //logger.info("CalculateBalanceAllATMCommand.execute");
        return (Number) receiver.execute();
    }
}
