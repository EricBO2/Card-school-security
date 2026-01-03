package se.sti.card_school_security.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import se.sti.card_school_security.model.authority.UserRole;

import java.util.UUID;

@Table("user_roles")
public class UserRoleEntity {

    private UUID userId;
    private UserRole role;

    public UserRoleEntity() {
    }

    public UserRoleEntity(UUID userId, UserRole role) {
        this.userId = userId;
        this.role = role;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
