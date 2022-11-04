package org.example.application.command.handle;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.example.generic.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.function.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.BiFunction;

@Component
class ErrorHandler {


    private static final BiFunction<HttpStatus, String, Mono<ServerResponse>> response =
            (status, value) -> ServerResponse.status(status).body(Mono.just(new ErrorResponse(value)),
                    ErrorResponse.class);

    Mono<ServerResponse> notFound(ServerRequest request) {
        return response.apply(HttpStatus.NOT_FOUND, "not found");
    }

    Mono<ServerResponse> badRequest(Throwable error) {
        if(error instanceof ServerWebInputException){
            var err= (ServerWebInputException)error;
            return response.apply(HttpStatus.BAD_REQUEST, Objects.requireNonNull(err.getRootCause()).getMessage());
        }
        return response.apply(HttpStatus.BAD_REQUEST, error.getMessage());
    }
}