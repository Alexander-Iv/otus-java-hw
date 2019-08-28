package alexander.ivanov.ms.config;

import alexander.ivanov.ms.MessageClient;
import alexander.ivanov.ms.MessageSystem;
import alexander.ivanov.ms.MessageSystemImpl;
import alexander.ivanov.ms.services.impl.DbService;
import alexander.ivanov.ms.services.impl.FeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class MessageSystemConfig {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemConfig.class);

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public DbService dbService(MessageSystem messageSystem) {
        return new DbService(messageSystem);
    }

    @Bean
    public FeService feService(MessageSystem messageSystem) {
        return new FeService(messageSystem);
    }

    @Bean
    public MessageSystem messageSystemInit(MessageSystem messageSystem, MessageClient...clients) {
        logger.info("MessageSystemConfig.messageSystemInit");
        for (MessageClient client : clients) {
            logger.info("client = {}", client);
            client.init();
        }
        messageSystem.init();
        return messageSystem;
    }
}
