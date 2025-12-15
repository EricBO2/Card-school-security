package se.sti.card_school_security.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomUserLoginDTO(
        @Size(min = 3,max = 254, message = "Incorrect email adress")
        @NotBlank(message = "Can't be blank")
        @Pattern(
                regexp = "^[\\p{L}\\p{N}._%+-]+@[\\p{L}\\p{N}._%+-]+\\.[\\p{L}]{2,}$",
                message = "Incorrect email adress"
        )
        String email,
        String password
){
}
