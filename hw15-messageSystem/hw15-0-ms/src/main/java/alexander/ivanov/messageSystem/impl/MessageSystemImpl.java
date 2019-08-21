package alexander.ivanov.messageSystem.impl;

import alexander.ivanov.messageSystem.Address;
import alexander.ivanov.messageSystem.Message;
import alexander.ivanov.messageSystem.MessageSystem;
import alexander.ivanov.messageSystem.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public final class MessageSystemImpl implements MessageSystem {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemImpl.class);

    private final List<Thread> workers;
    private final Map<Address, LinkedBlockingQueue<Message>> messages;
    private final Map<Address, Receiver> receivers;

    public MessageSystemImpl() {
        logger.debug("MessageSystemImpl.MessageSystemImpl");
        workers = new ArrayList<>();
        messages = new HashMap<>();
        receivers = new HashMap<>();
    }

    @Override
    public void addReceiver(Receiver receiver) {
        receivers.put(receiver.getAddress(), receiver);
        messages.put(receiver.getAddress(), new LinkedBlockingQueue<>());
    }

    @Override
    public void sendMessage(Message message) {
        logger.info("message.getFrom() = {}, message.getTo() = {}", message.getFrom(), message.getTo());

        receivers.forEach((address, receiver) -> {
            logger.info("address = {}, receiver = {}", address, receiver);
        });
        if (messages.containsKey(message.getTo())) {
            logger.debug("Contains {} address", message.getFrom());
        } else {
            logger.debug("Non contains {} address", message.getFrom());
        }
        messages.get(message.getTo()).add(message);

        messages.forEach((address, messages1) -> {
            logger.info("address = {}, messages1 = {}", address, messages1);
        });
    }

    @Override
    public void start() {
        logger.debug("MessageSystemImpl.start");
        receivers.entrySet().forEach(addressReceiverEntry -> {
            String name = "MS-worker-" + addressReceiverEntry.getKey().getName();
            Thread worker = new Thread(() -> {
                while (true) {
                    LinkedBlockingQueue<Message> workerQueue = messages.get(addressReceiverEntry.getKey());
                    while(true) {
                        try {
                            Message message = workerQueue.take();
                            message.process(addressReceiverEntry.getValue());
                        } catch (InterruptedException e) {
                            logger.warn("Thread interrupted. Finishing: {}", name);
                            return;
                        }
                        return;
                    }
                }
            });
            worker.setName(name);
            worker.start();
            logger.debug("worker {} started", name);
            workers.add(worker);
        });
    }

    @Override
    public void stop() {
        workers.forEach(Thread::interrupt);
    }

    /*@Override
    public String toString() {
        return "MessageSystemImpl{" +
                "workers=" + workers +
                ", messages=" + messages +
                ", receivers=" + receivers +
                '}';
    }*/
}
