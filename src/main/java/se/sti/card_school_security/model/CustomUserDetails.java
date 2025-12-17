package se.sti.card_school_security.model;

import io.jsonwebtoken.lang.Assert;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomUserDetails implements UserDetails {
    private final CustomUser customUser;

    public CustomUserDetails(CustomUser customUser) {
        Assert.notNull(customUser.getEmail(), "User email must not be null");
        Assert.notEmpty(customUser.getRoles(), "User must have roles");
        Assert.hasText(customUser.getPassword(), "User password must not be empty");
        this.customUser = customUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final Set<GrantedAuthority> authorities = new HashSet<>();

        customUser.getRoles().forEach(
                userRole -> authorities.addAll(userRole.getGrantedAuthorities())
        );
        return Collections.unmodifiableSet(authorities);
    }


    @Override
    public @Nullable String getPassword() {
        return customUser.getPassword();
    }

    @Override
    public String getUsername() {
        return customUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return customUser.isAccountNonExistent();
    }

    @Override
    public boolean isAccountNonLocked() {
        return customUser.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return customUser.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return customUser.isEnabled();
    }
}
