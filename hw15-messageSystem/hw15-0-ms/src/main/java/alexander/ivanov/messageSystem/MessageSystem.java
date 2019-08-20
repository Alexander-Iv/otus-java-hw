package alexander.ivanov.messageSystem;

public interface MessageSystem {
    void addReceiver(Receiver receiver);
    void sendMessage(Message message);
    void start();
    void stop();
}
