package org.example.application.queries;


import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.generic.GsonEventSerializer;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.*;

import javax.annotation.PostConstruct;
import java.util.Arrays;


@Configuration
public class ApplicationConfig {
    private final ConfigProperties configProperties;
    private final AmqpAdmin amqpAdmin;

    public ApplicationConfig(ConfigProperties configProperties, AmqpAdmin amqpAdmin) {
        this.configProperties = configProperties;
        this.amqpAdmin = amqpAdmin;
    }

    @PostConstruct
    public void init() {
        var exchange = new TopicExchange(configProperties.getExchange());
        var queue = new Queue(configProperties.getQueue(), false, false, true);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(configProperties.getRoutingKey()));
    }

    @Bean
    public Mono<Connection> connectionMono(@Value("spring.application.name") String name) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.useNio();
        return Mono.fromCallable(() -> connectionFactory.newConnection(name)).cache();
    }

    @Bean
    public SenderOptions senderOptions(Mono<Connection> connectionMono) {
        return new SenderOptions()
                .connectionMono(connectionMono)
                .resourceManagementScheduler(Schedulers.boundedElastic());
    }

    @Bean
    public Sender sender(SenderOptions senderOptions) {
        return RabbitFlux.createSender(senderOptions);
    }


    @Bean
    public ReceiverOptions receiverOptions(Mono<Connection> connectionMono) {
        return new ReceiverOptions()
                .connectionMono(connectionMono)
                .connectionSubscriptionScheduler(Schedulers.boundedElastic());
    }

    @Bean
    public Receiver receiver(ReceiverOptions receiverOptions) {
        return RabbitFlux.createReceiver(receiverOptions);
    }


    @Bean
    public GsonEventSerializer gsonEventSerializer() {
        return new GsonEventSerializer();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("*"));
        corsConfig.setMaxAge(8000L);
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

}

