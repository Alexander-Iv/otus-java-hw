package alexander.ivanov.ms;

public class MessageImpl extends Message {
    private final String clientName;
    private final String data;

    public MessageImpl(String clientName, String data) {
        this.clientName = clientName;
        this.data = data;
    }

    @Override
    public String process() {
        return data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "for='" + clientName + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
