package org.example.application.proxy.bus;


import org.example.application.proxy.ConfigProperties;
import org.example.application.proxy.SocketController;
import org.example.generic.EventSerializer;
import org.example.generic.Notification;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.ConsumeOptions;
import reactor.rabbitmq.ExceptionHandlers;
import reactor.rabbitmq.Receiver;

import java.time.Duration;


@Component
public class RabbitMQEventConsumer implements CommandLineRunner {

    private final Receiver receiver;
    private final ConfigProperties configProperties;
    private final EventSerializer eventSerializer;
    private final SocketController socketController;

    public RabbitMQEventConsumer(Receiver receiver, ConfigProperties configProperties, EventSerializer eventSerializer, SocketController socketController) {
        this.receiver = receiver;
        this.configProperties = configProperties;
        this.eventSerializer = eventSerializer;
        this.socketController = socketController;
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
                        var event = eventSerializer.deserialize(
                                notification.getBody(), Class.forName(notification.getType())
                        );
                        socketController.send(event.aggregateRootId(), event);
                        message.ack();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


                });
    }
}
