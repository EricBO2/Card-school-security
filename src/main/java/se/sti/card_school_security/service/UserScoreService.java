package se.sti.card_school_security.service;


import org.springframework.stereotype.Service;
import se.sti.card_school_security.model.dto.PlayerScoreDTO;


@Service

public class UserScoreService {

    // Här skulle du ha repository för DB (t.ex. JPA)

    public void saveResult(PlayerScoreDTO result) {

        // EX: repository.save(...)

        System.out.println("Saving result for player: " + result.score());

    }

}


