package org.example.application.command.adapter.bus;


import org.example.application.command.ConfigProperties;
import org.example.generic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

@Service
public class RabbitMQEventPublisher implements EventPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQEventPublisher.class);

    private final Sender sender;
    private final EventSerializer eventSerializer;
    private final ConfigProperties configProperties;

    public RabbitMQEventPublisher(Sender sender, EventSerializer eventSerializer, ConfigProperties configProperties) {
        this.sender = sender;
        this.eventSerializer = eventSerializer;
        this.configProperties = configProperties;
    }

    @Override
    public void publish(DomainEvent event) {
        sender.sendWithPublishConfirms(buildOutboundMessage(event))
                .doOnError(e -> LOGGER.error("Send failed", e))
                .subscribe(m -> {
                    if (m.isAck()) {
                        LOGGER.info("Message sent " + event.type);
                    }
                });

    }

    @Override
    public void publishError(Throwable errorEvent) {
        var event = new ErrorEvent(errorEvent.getClass().getTypeName(), errorEvent.getMessage());
        sender.sendWithPublishConfirms(buildOutboundMessage(event))
                .doOnError(e -> LOGGER.error("Send failed", e))
                .subscribe(m -> {
                    if (m.isAck()) {
                        LOGGER.error("Message sent " + event.type);
                    }
                });
    }

    private Mono<OutboundMessage> buildOutboundMessage(DomainEvent event) {
        var notification = new Notification(
                event.getClass().getTypeName(),
                eventSerializer.serialize(event)
        );
        return Mono.just(new OutboundMessage(
                configProperties.getExchange(), event.type, notification.serialize().getBytes()
        ));
    }


}