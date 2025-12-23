package se.sti.card_school_security.model.dto;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import se.sti.card_school_security.security.rules.EmailRules;
import se.sti.card_school_security.security.rules.PasswordRules;
import se.sti.card_school_security.security.rules.UsernameRules;

public class CustomUserRegisterDTO {
    @Valid
    private EmailRules email;
    @Valid
    private PasswordRules password;
    @Valid
    private UsernameRules username;

    public CustomUserRegisterDTO(UsernameRules username, PasswordRules password, EmailRules email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public CustomUserRegisterDTO() {
    }

    public String getEmail() {
        return email.value();
    }

    public void setEmail(String email) {
        this.email = new EmailRules(email);
    }

    public String getPassword() {
        return password.value();
    }

    public void setPassword(String password) {
        this.password = new PasswordRules(password);
    }

    public String getUsername() {
        return username.value();
    }

    public void setUsername(String username) {
        this.username = new UsernameRules(username);
    }
}
