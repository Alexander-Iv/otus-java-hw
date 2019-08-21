package alexander.ivanov.dbservice.database.services.messages.toFe;

import alexander.ivanov.messageSystem.Address;
import alexander.ivanov.messageSystem.Message;
import alexander.ivanov.messageSystem.MessageSystemContext;
import alexander.ivanov.messageSystem.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IsIncorrectUser implements Message {
    private static final Logger logger = LoggerFactory.getLogger(IsIncorrectUser.class);
    private final MessageSystemContext context;

    public IsIncorrectUser(MessageSystemContext context) {
        this.context = context;
    }

    @Override
    public Address getFrom() {
        return null;
    }

    @Override
    public Address getTo() {
        return null;
    }

    @Override
    public void process(Receiver receiver) {
        logger.debug("IsIncorrectUser process");
    }
}
