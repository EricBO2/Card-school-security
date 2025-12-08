package se.sti.card_school_security.security.rules;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import se.sti.card_school_security.model.authority.UserRole;

import java.util.Set;

public record RolesRules (
        @NotEmpty
        @Pattern(
                regexp = "^(EMPLOYEE|ADMIN)",
                message = "Must be a valid role"
        )
        Set<UserRole> value
){
}
