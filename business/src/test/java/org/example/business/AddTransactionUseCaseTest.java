package org.example.business;

import com.google.gson.Gson;
import org.example.domain.command.AddTransactionUseCommand;
import org.example.domain.events.AccountCreated;
import org.example.domain.events.TransactionAdded;
import org.example.domain.value.AccountId;
import org.example.domain.value.Name;
import org.example.domain.value.TransactionId;
import org.example.domain.value.UserId;
import org.example.generic.business.EventStoreRepository;
import org.example.generic.domain.DomainEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddTransactionUseCaseTest {

    @InjectMocks
     AddTransactionUseCase useCase;

    @Mock
    EventStoreRepository repository;

    @Test
    void addTransaction(){
        var id = AccountId.of("xxxx");
        var command = new AddTransactionUseCommand(id, new Date());
        when(repository.getEventsBy(any(), any())).thenReturn(storedEvent());
        StepVerifier.create(useCase.apply(Mono.just(command)))
                .expectNextMatches((domainEvent -> {
                    var event = (TransactionAdded)domainEvent;
                    return event.type.equals("org.example.TransactionAdded");
                }))
                .expectComplete()
                .verify();
    }

    private Flux<DomainEvent> storedEvent() {
        return Flux.just(
                new AccountCreated(new UserId(), new Name("Raul Alzate")),
                new TransactionAdded(new TransactionId(), new Date())
        );
    }
}