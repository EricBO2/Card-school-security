package se.sti.card_school_security.model.authority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static se.sti.card_school_security.model.authority.UserPermission.*;

public enum UserRole {

    GUST(
            UserRoleName.GUST.getRoleName(),
            Set.of(
                    CREATE_PLAYER
            )),
    PLAYER(
            UserRoleName.PLAYER.getRoleName(),
            Set.of(
                    DELETE_SELF,
                    PLAY
            )),
    ADMIN(
            UserRoleName.ADMIN.getRoleName(),
            Set.of(
                    DELETE_SELF,
                    DELETE_OTHER,
                    PLAY,
                    CREATE_ADMIN,
                    CREATE_PLAYER,
                    BAN_PLAYER
            )
    );



    private String roleName;
    private Set<UserPermission> userPermissions;

    UserRole(String roleName, Set<UserPermission> userPermissions) {
        this.roleName = roleName;
        this.userPermissions = userPermissions;
    }

    public String getRoleName() {
        return roleName;
    }

    public Set<UserPermission> getUserPermissions() {
        return userPermissions;
    }

    public List<SimpleGrantedAuthority> getGrantedAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(roleName));
        authorities.addAll(
                this.userPermissions.stream().map(
                        userPermission -> new SimpleGrantedAuthority(userPermission.getUserPermission())
                ).toList()
        );

        return authorities;
    }
}
