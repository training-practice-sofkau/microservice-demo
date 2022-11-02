package org.example.business.gateway;

import org.example.domain.value.Name;
import reactor.core.publisher.Mono;

public interface ValidOwnerOfAccountService {
    Mono<Boolean> valid(Name name);
}
