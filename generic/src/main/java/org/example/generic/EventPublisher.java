package org.example.generic;


public interface EventPublisher {

    void publish(DomainEvent event);

    void publishError(Throwable errorEvent);
}