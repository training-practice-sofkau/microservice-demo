package org.example.generic.infrastructure;

import org.example.generic.domain.DomainEvent;
import reactor.core.publisher.Mono;

public interface DelegateService {
   Mono<Void> doProcessing(DomainEvent input);
}