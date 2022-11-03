package org.example.application.command;


import org.example.domain.events.AccountCreated;
import org.example.generic.DelegateService;
import org.example.generic.domain.DomainEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class BusinessLookUp {
    private final Map<String, Flux<DelegateService>> business = new HashMap<>();

    public BusinessLookUp(ApplicationContext context) {
        business.put("org.example.AccountCreated", Flux.just( input -> {
            var event = (AccountCreated)input;
            System.out.println(event);
            return Mono.empty();
        }));
    }

    public Flux<DelegateService> get(String eventType) {
        return business.getOrDefault(eventType, Flux.empty());
    }
}
