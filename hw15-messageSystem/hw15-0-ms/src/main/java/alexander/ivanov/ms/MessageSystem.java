package alexander.ivanov.ms;

public interface MessageSystem {
    void init();
    void sendMessage(Message message);
    void addClient(MessageClient client);
    Message createMessageFor(String clientName, String data);
}
