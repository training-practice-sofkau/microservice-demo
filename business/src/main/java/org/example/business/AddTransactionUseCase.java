package org.example.business;

import org.example.business.gateway.AccountRepository;
import org.example.domain.Account;
import org.example.domain.command.AddTransactionUseCommand;
import org.example.domain.value.TransactionId;
import org.example.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class AddTransactionUseCase implements Function<Mono<AddTransactionUseCommand>, Flux<DomainEvent>> {

    private final AccountRepository accountRepository;

    public AddTransactionUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Flux<DomainEvent> apply(Mono<AddTransactionUseCommand> commandMono) {
        return commandMono.flatMapMany(command -> {
            var id = command.getId();
            return accountRepository.findEventBy(id).collectList().flatMapIterable(events -> {
                var account = Account.from(id, events);
                if(account.transactions().size() == 10){
                    throw new IllegalArgumentException("Business Error");
                }
                account.addTransaction(new TransactionId(), command.getDate());
                return account.getUncommittedChanges();
            });
        });
    }
}
