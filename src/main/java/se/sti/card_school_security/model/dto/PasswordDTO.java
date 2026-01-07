package se.sti.card_school_security.model.dto;

import se.sti.card_school_security.security.rules.PasswordRules;

public class PasswordDTO {
    private PasswordRules password;

    public PasswordDTO(String password) {
        this.password = new PasswordRules(password);
    }

    public String getPassword() {
        return password.value();
    }

    public void setPassword(String password) {
        this.password = new PasswordRules(password);
    }
}
