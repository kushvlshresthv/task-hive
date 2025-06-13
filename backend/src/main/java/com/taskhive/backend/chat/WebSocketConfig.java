package com.taskhive.backend.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

//@Configuration
//@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        //enables in-memory message broker which handles the "/user" as the destination prefix
        registry.enableSimpleBroker("/user");

        //strips the /app from the STOMP message header and sends the request to @MessageMapping
        registry.setApplicationDestinationPrefixes("/app");

        //setUserDestinationPrefix("/user") is what tells Spring: "Intercept any messages or subscriptions with /user and let the UserDestinationMessageHandler rewrite them"
        registry.setUserDestinationPrefix("/user");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }


    /**
     * NOTE: Spring boot already configures MappingJackson2MessageConverter
     * We don't have to set a custom MappingJackson2MessageConverter like below, unless we have a specific customization need
     */

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        //create the content resolver and set the content type
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(APPLICATION_JSON);

        //create the actual converter
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());

        //set the converter to the list of converters
        converter.setContentTypeResolver(resolver);

        messageConverters.add(converter);

        //returning false because we don't want to override the default message converters provided by Spring instead, we are just adding to the existing list

        return false;
    }
}
