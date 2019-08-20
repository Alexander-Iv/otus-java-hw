package alexander.ivanov.dbservice.database.services.messages.toFe;

import alexander.ivanov.messageSystem.Address;
import alexander.ivanov.messageSystem.Message;
import alexander.ivanov.messageSystem.Receiver;

public class IsIncorrectUser implements Message {
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

    }
}
