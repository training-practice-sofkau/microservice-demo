package org.example.application.queries;

import org.example.generic.DelegateService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@Component
public class MaterializeLookUp {
    private final Map<String, Flux<DelegateService>> business = new HashMap<>();


    public MaterializeLookUp(ApplicationContext context) {

    }


    public Flux<DelegateService> get(String eventType) {
        return business.getOrDefault(eventType, Flux.empty());
    }
}
