package se.sti.card_school_security.security.rules;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordRules (
        @Pattern(
                regexp = "^" +
                        "(?=.*[a-z])" +
                        "(?=.*[A-Z])" +
                        "(?=.*[0-9])" +
                        "(?=.*[ @$!%*?&])" +
                        ".+$",
                message = "Password must contain at least one uppercase, one lowercase, one digit, and one special character"
        )
        @Size(max = 80, message = "Maximum value length has exceeded")
        String value
) {
}
