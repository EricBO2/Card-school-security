package se.sti.card_school_security.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import se.sti.card_school_security.model.authority.UserRole;

import java.util.Set;
import java.util.UUID;

@Table("user")
public class CustomUser {
    @Id
    private UUID id = UUID.randomUUID();

    private int score;

    private String username;
    private String email;
    private String password;

    private boolean isAccountNonExistent;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    private Set<UserRole> roles;

    public CustomUser() {
    }


    public CustomUser(int score, String username, String email, String password, boolean isAccountNonExistent, boolean isAccountNonLocked, boolean isCredentialsNonExpired, boolean isEnabled, Set<UserRole> roles) {
        this.score = score;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAccountNonExistent = isAccountNonExistent;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.roles = roles;
    }




    public String getUsername() {
        return username;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAccountNonExistent() {
        return isAccountNonExistent;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountNonExistent(boolean accountNonExistent) {
        isAccountNonExistent = accountNonExistent;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public static CustomUser create(
            int score,
            String username,
            String email,
            String password,
            Set<UserRole> roles
    ) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("User must have roles");
        }

        CustomUser user = new CustomUser();
        user.id = UUID.randomUUID();
        user.score = score;
        user.username = username;
        user.email = email;
        user.password = password;
        user.roles = roles;

        user.isAccountNonExistent = true;
        user.isAccountNonLocked = true;
        user.isCredentialsNonExpired = true;
        user.isEnabled = true;

        return user;
    }
}

