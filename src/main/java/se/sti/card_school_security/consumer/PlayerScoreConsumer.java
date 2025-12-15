package se.sti.card_school_security.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import se.sti.card_school_security.model.dto.PlayerScoreDTO;

@Component
public class PlayerScoreConsumer {

    @RabbitListener(queues = "player-scores")
    public void receiveScore(PlayerScoreDTO score) {
        System.out.println("Received player score: " + score.score());
        // Här kan du spara poängen i databas, ex:
        // scoreRepository.save(score);
    }
}
