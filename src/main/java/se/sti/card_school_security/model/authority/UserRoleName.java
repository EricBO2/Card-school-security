package se.sti.card_school_security.model.authority;

public enum UserRoleName {

    GUST("ROLE_GUST"),
    PLAYER("ROLE_PLAYER"),
    ADMIN("ROLE_ADMIN");

    private final String roleName;

    UserRoleName(String roleName) {
        this.roleName = roleName;
    }
    public String getRoleName() {
        return roleName;
    }
}
