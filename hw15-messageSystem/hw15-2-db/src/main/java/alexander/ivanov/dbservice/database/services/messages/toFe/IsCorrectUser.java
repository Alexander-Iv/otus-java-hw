package alexander.ivanov.dbservice.database.services.messages.toFe;

import alexander.ivanov.messageSystem.Address;
import alexander.ivanov.messageSystem.Message;
import alexander.ivanov.messageSystem.MessageSystemContext;
import alexander.ivanov.messageSystem.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IsCorrectUser implements Message {
    private static final Logger logger = LoggerFactory.getLogger(IsCorrectUser.class);
    private final MessageSystemContext context;

    public IsCorrectUser(MessageSystemContext context) {
        this.context = context;
    }

    @Override
    public Address getFrom() {
        return context.getDbAddress();
    }

    @Override
    public Address getTo() {
        return context.getFeAddress();
    }

    @Override
    public void process(Receiver receiver) {
        logger.debug("IsCorrectUser process");
    }
}
