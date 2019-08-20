package alexander.ivanov.dbservice.database.services.messages.toFe;

import alexander.ivanov.dbservice.database.hibernate.model.User;
import alexander.ivanov.messageSystem.Address;
import alexander.ivanov.messageSystem.Message;
import alexander.ivanov.messageSystem.MessageSystemContext;
import alexander.ivanov.messageSystem.Receiver;
import alexander.ivanov.messageSystem.services.FeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthUserMessage implements Message {
    private static final Logger logger = LoggerFactory.getLogger(AuthUserMessage.class);

    private final MessageSystemContext context;
    private final User user;

    public AuthUserMessage(MessageSystemContext context, User user) {
        this.context = context;
        this.user = user;
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
        logger.debug("AuthUserMessage.process");
        FeService feService = (FeService)receiver;
        feService.auth(user.getName(), user.getPassword());
        logger.info("feService = {}", feService);
    }
}
