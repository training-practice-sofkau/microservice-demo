package org.example.application.command;

import com.rabbitmq.client.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
public class AppCommand {
    private final Mono<Connection> connectionMono;

    public AppCommand(Mono<Connection> connectionMono) {
        this.connectionMono = connectionMono;
    }

    public static void main(String[] args) {
        SpringApplication.run(AppCommand.class, args);
    }

    @PreDestroy
    public void close() throws IOException {
        Objects.requireNonNull(connectionMono.block()).close();
    }
}
