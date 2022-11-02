package org.example.business;

import org.example.business.gateway.ValidOwnerOfAccountService;
import org.example.domain.Account;
import org.example.domain.command.CreateAccountCommand;
import org.example.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class CreateAccountUseCase implements Function<Mono<CreateAccountCommand>, Flux<DomainEvent>> {

    private final ValidOwnerOfAccountService service;

    public CreateAccountUseCase(ValidOwnerOfAccountService service) {
        this.service = service;
    }

    @Override
    public Flux<DomainEvent> apply(Mono<CreateAccountCommand> commandMono) {

        return commandMono.flatMapMany(command -> service.valid(command.getName()).flatMapIterable(isValid -> {
            if(!isValid.equals(Boolean.TRUE)){
              throw new IllegalArgumentException("Business Exception");
            }
            var account = new Account(command.getId(), command.getUserId(), command.getName());
            return account.getUncommittedChanges();
        }));
    }
}
