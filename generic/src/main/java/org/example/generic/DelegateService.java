package org.example.generic;

import org.example.generic.domain.DomainEvent;
import reactor.core.publisher.Mono;

public interface DelegateService {
   Mono<Void> doProcessing(DomainEvent input);
}