package se.sti.card_school_security.model.dto;

// Sends player score to security microservice through RabbitMQ
public record PlayerScoreDTO(
        int score
) {}


