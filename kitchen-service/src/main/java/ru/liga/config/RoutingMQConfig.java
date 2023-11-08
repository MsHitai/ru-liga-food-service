package ru.liga.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingMQConfig {

    @Bean
    public Declarables myQueue() {
        Queue firstQueue = new Queue("courier1", false);
        Queue secondQueue = new Queue("courier2", false);
        Queue thirdQueue = new Queue("order", false);
        Queue fourthQueue = new Queue("kitchen1", false);
        Queue fifthQueue = new Queue("kitchen2", false);
        DirectExchange directExchange = new DirectExchange("directExchange");

        return new Declarables(firstQueue, secondQueue, thirdQueue, fourthQueue, fifthQueue, directExchange,
                BindingBuilder.bind(firstQueue).to(directExchange).with("courier.pick"),
                BindingBuilder.bind(secondQueue).to(directExchange).with("courier.find"),
                BindingBuilder.bind(thirdQueue).to(directExchange).with("order.status"),
                BindingBuilder.bind(fourthQueue).to(directExchange).with("kitchen.cook"),
                BindingBuilder.bind(fifthQueue).to(directExchange).with("status.update"));
    }
}
