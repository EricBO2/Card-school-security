package se.sti.card_school_security.model;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.sti.card_school_security.model.dto.CustomUserDetailsDTO;

import java.util.*;

public class CustomUserDetails implements UserDetails {
    private final CustomUserDetailsDTO customUserDTO;

    public CustomUserDetails(CustomUserDetailsDTO customUserDTO) {
        this.customUserDTO = customUserDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final Set<GrantedAuthority> authorities = new HashSet<>();

        customUserDTO.getRoles().forEach(
                userRole -> authorities.addAll(userRole.getGrantedAuthorities())
        );
        return Collections.unmodifiableSet(authorities);
    }

    @Override
    public @Nullable String getPassword() {
        return customUserDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return customUserDTO.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return customUserDTO.accountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return customUserDTO.accountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return customUserDTO.credentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return customUserDTO.enabled();
    }
}
