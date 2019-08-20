package alexander.ivanov.messageSystem.config;

import alexander.ivanov.messageSystem.Address;
import alexander.ivanov.messageSystem.MessageSystem;
import alexander.ivanov.messageSystem.MessageSystemContext;
import alexander.ivanov.messageSystem.impl.MessageSystemImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class MessageSystemConfig {
    @Bean
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
        return context;
    }
}
