package se.sti.card_school_security.model.dto;

import se.sti.card_school_security.model.authority.UserRole;

import java.util.Set;

public record UserResponseDTO(
        int score,

        String username,
        String email,
        Set<String> roles
) {
}
