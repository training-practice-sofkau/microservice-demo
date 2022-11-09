package org.example.application.command.handle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.business.AddTransactionUseCase;
import org.example.business.CreateAccountUseCase;
import org.example.domain.command.AddTransactionUseCommand;
import org.example.domain.command.CreateAccountCommand;
import org.example.domain.events.TransactionAdded;
import org.example.domain.value.AccountId;
import org.example.domain.value.Name;
import org.example.domain.value.UserId;
import org.example.generic.business.IntegrationHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CommandHandle {
    private final IntegrationHandle integrationHandle;
    private final ErrorHandler errorHandler;
    public CommandHandle(IntegrationHandle integrationHandle, ErrorHandler errorHandler) {
        this.integrationHandle = integrationHandle;
        this.errorHandler = errorHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> crear(CreateAccountUseCase usecase) {

        return route(
                POST("/account/create").and(accept(MediaType.APPLICATION_JSON)),

                request -> usecase
                        .apply(request.bodyToMono(CreateAccountCommand.class))
                        .collectList()
                        .flatMap(list -> ServerResponse.accepted().bodyValue(list))
                        .onErrorResume(errorHandler::badRequest)

        );
    }


    @Bean
    public RouterFunction<ServerResponse> addtransaction(AddTransactionUseCase usecase) {

        return route(
                POST("/account/addtransaction").and(accept(MediaType.APPLICATION_JSON)),

                request -> usecase.andThen(integrationHandle)
                        .apply(request.bodyToMono(AddTransactionUseCommand.class))
                        .then(ServerResponse.ok().build())
                        .onErrorResume(errorHandler::badRequest)

        );
    }
}
