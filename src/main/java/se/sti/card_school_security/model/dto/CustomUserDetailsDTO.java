package se.sti.card_school_security.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import se.sti.card_school_security.model.authority.UserRole;
import se.sti.card_school_security.security.rules.EmailRules;
import se.sti.card_school_security.security.rules.PasswordRules;
import se.sti.card_school_security.security.rules.RolesRules;

import java.util.Set;

public class CustomUserDetailsDTO {
    @Valid
    EmailRules email;

    @Valid
    PasswordRules password;

    @NotNull boolean accountNonExpired;
    @NotNull boolean accountNonLocked;
    @NotNull boolean credentialsNonExpired;
    @NotNull boolean enabled;

    @Valid
    RolesRules roles;

    public CustomUserDetailsDTO(String email, String password, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, Set<UserRole> roles) {
        this.email = new EmailRules(email);
        this.password = new PasswordRules(password);
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.roles = new RolesRules(roles);
    }

    public String getEmail() {
        return email.value();
    }

    public String getPassword() {
        return password.value();
    }

    public boolean accountNonExpired() {
        return accountNonExpired;
    }

    public boolean accountNonLocked() {
        return accountNonLocked;
    }

    public boolean credentialsNonExpired() {
        return credentialsNonExpired;
    }

    public boolean enabled() {
        return enabled;
    }

    public Set<UserRole> getRoles() {
        return roles.value();
    }

    public void setEmail(String email) {
        this.email = new EmailRules(email);
    }

    public void setPassword(String password) {
        this.password = new PasswordRules(password);
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = new RolesRules(roles);
    }
}
