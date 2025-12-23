package se.sti.card_school_security.security.rules;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsernameRules(
    @Size(min = 2,max = 25, message = "Too long/short first name")
    @NotBlank(message = "Can't be blank")
    String value
){
}
