package org.example.application.queries;

import com.rabbitmq.client.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
public class AppQuery {
    private final Mono<Connection> connectionMono;

    public AppQuery(Mono<Connection> connectionMono) {
        this.connectionMono = connectionMono;
    }

    public static void main(String[] args) {
        SpringApplication.run(AppQuery.class, args);
    }

    @PreDestroy
    public void close() throws IOException {
        Objects.requireNonNull(connectionMono.block()).close();
    }

}