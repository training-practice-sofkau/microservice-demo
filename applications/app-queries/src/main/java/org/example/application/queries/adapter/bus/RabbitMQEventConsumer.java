package org.example.application.queries.adapter.bus;


import org.example.application.queries.ConfigProperties;
import org.example.application.queries.MaterializeLookUp;
import org.example.generic.domain.DomainEvent;
import org.example.generic.infrastructure.GsonEventSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.ConsumeOptions;
import reactor.rabbitmq.ExceptionHandlers;
import reactor.rabbitmq.Receiver;

import java.time.Duration;


@Component
public class RabbitMQEventConsumer implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQEventConsumer.class);

    private final GsonEventSerializer serializer;
    private final Receiver receiver;
    private final ConfigProperties configProperties;
    private final MaterializeLookUp materializeLookUp;

    public RabbitMQEventConsumer(GsonEventSerializer serializer, Receiver receiver, ConfigProperties configProperties, MaterializeLookUp materializeLookUp) {
        this.serializer = serializer;
        this.receiver = receiver;
        this.configProperties = configProperties;
        this.materializeLookUp = materializeLookUp;
    }


    @Override
    public void run(String... args) {
        receiver.consumeManualAck(configProperties.getQueue(), new ConsumeOptions()
                .exceptionHandler(new ExceptionHandlers.RetryAcknowledgmentExceptionHandler(
                        Duration.ofSeconds(20), Duration.ofMillis(500),
                        ExceptionHandlers.CONNECTION_RECOVERY_PREDICATE
                )))
                .subscribe(message -> {
                    var notification = Notification.from(new String(message.getBody()));
                    try {
                        DomainEvent event = serializer.deserialize(
                                notification.getBody(), Class.forName(notification.getType())
                        );
                        LOGGER.info("Receiver => " + event.type);
                        materializeLookUp.get(event.type)
                                .flatMap(materializeService -> materializeService.doProcessing(event))
                                .subscribe(unused -> {
                                    message.ack();
                                });
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
    }
}
