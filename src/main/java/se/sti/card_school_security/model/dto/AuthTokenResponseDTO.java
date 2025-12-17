package se.sti.card_school_security.model.dto;

import java.util.concurrent.TimeUnit;

public record AuthTokenResponseDTO(
        String accessToken,
        String tokenType,
        Long expiresIn
) {
    public AuthTokenResponseDTO(String accessToken) {
        // Sätter standardvärdet för tokenType
        this(accessToken, "Bearer", (Long) TimeUnit.MINUTES.toMillis(10));
    }
}
