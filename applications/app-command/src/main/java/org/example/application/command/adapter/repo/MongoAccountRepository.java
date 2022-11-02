package org.example.application.command.adapter.repo;

import org.example.business.gateway.AccountRepository;
import org.example.domain.value.AccountId;
import org.example.generic.DomainEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class MongoAccountRepository implements AccountRepository {
    @Override
    public Flux<DomainEvent> findEventBy(AccountId accountId) {
        return null;
    }
}
