package se.sti.card_school_security.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String GAME_RESULTS_QUEUE = "game-results";

    @Bean
    public Queue gameResultsQueue() {
        return new Queue(GAME_RESULTS_QUEUE, true); // true = durable
    }
}
