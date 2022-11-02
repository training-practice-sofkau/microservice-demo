package org.example.application.command.adapter.service;

import org.example.business.gateway.ValidOwnerOfAccountService;
import org.example.domain.value.Name;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RestValidOwnerOfAccountService implements ValidOwnerOfAccountService {
    @Override
    public Mono<Boolean> valid(Name name) {
        return Mono.just(true);
    }
}
