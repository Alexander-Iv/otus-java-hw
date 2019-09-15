package alexander.ivanov.ms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

//@Component
public class MessageSystemImpl implements MessageSystem {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemImpl.class);
    private final int CAPACITY_DEFAULT = 10;

    private final ArrayBlockingQueue<MessageClient> clients = new ArrayBlockingQueue<>(CAPACITY_DEFAULT);
    private final ConcurrentMap<MessageClient, ArrayBlockingQueue<Message>> clientQueueMessage = new ConcurrentHashMap<>();
    private final ConcurrentMap<MessageClient, ExecutorService> clientExecutors = new ConcurrentHashMap<>();

    private final ArrayBlockingQueue<Message> queueInbox = new ArrayBlockingQueue<>(CAPACITY_DEFAULT);
    private final ExecutorService executorInbox = Executors.newSingleThreadExecutor();

    @Override
    public void init() {
        logger.info("MessageSystemImpl.init");
        clients.forEach(client -> {
            clientExecutors.put(client, Executors.newSingleThreadExecutor());
            clientQueueMessage.put(client, new ArrayBlockingQueue<>(CAPACITY_DEFAULT));
        });

        logger.info("Starting execute");
        executorInbox.execute(this::processMsgInbox);

        /*logger.info("Shutdown executors");
        executorInbox.shutdown();*/

        clientExecutors.forEach((client, executorService) -> {
            logger.info("client = {}, executorService = {}", client, executorService);
            executorService.execute(() -> this.processMsgOutbox(clientQueueMessage.get(client), client));
        });

        /*clientExecutors.forEach((client, executorService) -> {
            executorService.shutdown();
        });*/
    }

    private void processMsgInbox() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message msg = queueInbox.take();
                logger.info("new message:{}", msg);
                msg.getQueueTo().put(msg);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void processMsgOutbox(ArrayBlockingQueue<Message> queue, MessageClient client) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message msg = queue.take();
                client.accept(msg);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void sendMessage(Message message) {
        logger.info("sending message = {}", message);
        try {
            queueInbox.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("e.getMessage() = {}", e.getMessage());
        }
    }

    @Override
    public void addClient(MessageClient client) {
        logger.info("client.getName() = {}", client.getName());
        clients.add(client);
    }

    /*@Override
    public void createMessageFor(MessageClient target, String data) {
        logger.info("MessageSystemImpl.createMessageFor");
        logger.info("target = {}, data = {}", target, data);
        clients.forEach(client -> {
            if (client.equals(target)) {
                Message msg = new MessageImpl(target.getClass().getSimpleName(), data);
                ArrayBlockingQueue<Message> targetQueue = clientQueueMessage.get(target);
                logger.info("targetQueue = {}", targetQueue);
                msg.setQueueTo(targetQueue);
                target.accept(msg);
            }
        });
    }*/

    @Override
    public Message createMessageFor(String clientName, String data) {
        logger.info("MessageSystemImpl.createMessageFor");
        logger.info("clientName = {}, data = {}", clientName, data);
        MessageClient client = getClientByName(clientName);
        logger.info("messageClient = {}", client);
        ArrayBlockingQueue<Message> targetQueue = clientQueueMessage.get(client);
        Message message = new MessageImpl(clientName, data);
        message.setQueueTo(targetQueue);
        return message;
    }

    private MessageClient getClientByName(String name) {
        logger.info("MessageSystemImpl.getClientByName");
        return clients.stream()
                .filter(client -> client.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
