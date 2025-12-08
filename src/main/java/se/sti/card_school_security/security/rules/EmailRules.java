package se.sti.card_school_security.security.rules;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmailRules (
        @Size(min = 3,max = 254, message = "Too long/short value")
        @NotBlank(message = "Can't be blank")
        @Pattern(
                regexp = "^[\\p{L}\\p{N}._%+-]+@[\\p{L}\\p{N}._%+-]+\\.[\\p{L}]{2,}$",
                message = "Must be valid value"
        )
        String value
){
}
