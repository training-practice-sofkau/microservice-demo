package org.example.business.gateway;

import org.example.domain.value.AccountId;
import org.example.generic.DomainEvent;
import reactor.core.publisher.Flux;

public interface AccountRepository {
    Flux<DomainEvent> findEventBy(AccountId accountId);
}
