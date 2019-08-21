package alexander.ivanov.messageSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSystemContext {
    private final MessageSystem messageSystem;

    private Address feAddress;
    private Address dbAddress;

    @Autowired
    public MessageSystemContext(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        this.feAddress = new Address("fe-address");
        this.dbAddress = new Address("db-address");
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public Address getFeAddress() {
        return feAddress;
    }

    public void setFeAddress(Address feAddress) {
        this.feAddress = feAddress;
    }

    public Address getDbAddress() {
        return dbAddress;
    }

    public void setDbAddress(Address dbAddress) {
        this.dbAddress = dbAddress;
    }

    @Override
    public String toString() {
        return "MessageSystemContext{" +
                "messageSystem=" + messageSystem +
                ", feAddress=" + feAddress +
                ", dbAddress=" + dbAddress +
                '}';
    }
}
