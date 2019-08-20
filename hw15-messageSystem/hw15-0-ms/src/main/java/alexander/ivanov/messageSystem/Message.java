package alexander.ivanov.messageSystem;

public interface Message {
    Address getFrom();
    Address getTo();
    void process(Receiver receiver);
}
