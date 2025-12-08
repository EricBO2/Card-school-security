package se.sti.card_school_security.model.authority;

public enum UserPermission {
    PLAY("PLAY"),
    DELETE_SELF("DELETE_SELF"),
    DELETE_OTHER("DELETE_OTHER"),
    CREATE_PLAYER("CREATE_PLAYER"),
    CREATE_ADMIN("CREATE ADMIN"),
    BAN_PLAYER("BAN_PLAYER");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getUserPermission() {
        return permission;
    }
}
