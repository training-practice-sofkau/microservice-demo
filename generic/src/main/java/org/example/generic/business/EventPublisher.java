package org.example.generic.business;


import org.example.generic.domain.DomainEvent;

public interface EventPublisher {

    void publish(DomainEvent event);

    void publishError(Throwable errorEvent);
}