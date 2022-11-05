package org.example.application.queries.handle;


import org.example.application.queries.adapter.repo.AccountModeView;
import org.example.application.queries.adapter.repo.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class QueryHandle {

    private final AccountRepository repository;

    public QueryHandle(AccountRepository repository) {
        this.repository = repository;
    }

    @Bean
    public RouterFunction<ServerResponse> getAccount() {
        return route(
                GET("/account/{id}"),
                request -> repository.findById(request.pathVariable("id"))
                        .flatMap(account -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(account))
        );
    }

}
