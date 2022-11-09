package org.example.business;

import com.google.gson.Gson;
import org.example.domain.command.CreateAccountCommand;
import org.example.domain.events.AccountCreated;
import org.example.domain.value.AccountId;
import org.example.domain.value.Name;
import org.example.domain.value.UserId;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CreateAccountUseCaseTest {

    @Test
    void createAccount(){
        var usecase = new CreateAccountUseCase(name -> Mono.just(Boolean.TRUE));
        var command = new CreateAccountCommand(new AccountId(), new UserId(), new Name("Raul A. Alzate"));
        System.out.println(new Gson().toJson(command));;
        //var eventExpect = new AccountCreated(new UserId(), new Name("Raul A. Alzate"));
        StepVerifier.create(usecase.apply(Mono.just(command)))
                .expectNextMatches((domainEvent -> {
                    var event = (AccountCreated)domainEvent;
                    return event.getName().value().equals("Raul A. Alzate");
                }))
                .expectComplete()
                .verify();
    }

}