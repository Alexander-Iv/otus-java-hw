package alexander.ivanov.messageSystem.config;

import alexander.ivanov.messageSystem.Address;
import alexander.ivanov.messageSystem.MessageSystem;
import alexander.ivanov.messageSystem.MessageSystemContext;
import alexander.ivanov.messageSystem.impl.MessageSystemImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

//@Configuration
public class MessageSystemConfig {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemConfig.class);

    /*@Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    @DependsOn("messageSystem")
    public MessageSystemContext messageSystemContext(MessageSystem messageSystem) {
        MessageSystemContext context = new MessageSystemContext(messageSystem);
        Address dbAddress = new Address("db-address");
        context.setDbAddress(dbAddress);
        Address feAddress = new Address("fe-address");
        context.setFeAddress(feAddress);
        logger.debug("bean context = {}", context);
        return context;
    }*/
}
