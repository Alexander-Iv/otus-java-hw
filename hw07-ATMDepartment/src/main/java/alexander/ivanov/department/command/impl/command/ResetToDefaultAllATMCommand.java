package alexander.ivanov.department.command.impl.command;

import alexander.ivanov.department.command.Command;
import alexander.ivanov.department.command.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResetToDefaultAllATMCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ResetToDefaultAllATMCommand.class);
    private Receiver receiver;

    public ResetToDefaultAllATMCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public Boolean execute() {
        //logger.info("ResetToDefaultAllATMCommand.execute");
        return receiver.execute();
    }
}
