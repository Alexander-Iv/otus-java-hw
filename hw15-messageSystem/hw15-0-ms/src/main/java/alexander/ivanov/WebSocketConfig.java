package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.*;

@EnableAsync
@ComponentScan(basePackages = {
        "alexander.ivanov.ms", "alexander.ivanov.cache", "alexander.ivanov.dbservice", "alexander.ivanov.fe"
})
@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        logger.info("WebSocketConfig.configureMessageBroker");
        registry.enableSimpleBroker("/message-broker");
        registry.setApplicationDestinationPrefixes("/message-system");
        logger.info("registry = {}", registry);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        logger.info("WebSocketConfig.registerStompEndpoints");
        SockJsServiceRegistration ssr = registry.addEndpoint("/websocket").withSockJS();
        logger.info("ssr = {}", ssr);
    }
}
