package alexander.ivanov.ms.services.impl;

import alexander.ivanov.ms.Message;
import alexander.ivanov.ms.MessageClient;
import alexander.ivanov.ms.MessageSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
public class DbService implements MessageClient {
    private static final Logger logger = LoggerFactory.getLogger(DbService.class);
    private final MessageSystem ms;

    //@Autowired
    public DbService(MessageSystem ms) {
        this.ms = ms;
    }

    @Override
    public void init() {
        //this.ms.addClient(this);
    }

    @Override
    public void accept(Message msg) {
        logger.info("DbService.accept");
        logger.info("msg = {}", msg);
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("DbService.end");
    }
}
