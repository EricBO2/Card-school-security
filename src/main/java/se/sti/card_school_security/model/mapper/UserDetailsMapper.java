package se.sti.card_school_security.model.mapper;

import se.sti.card_school_security.model.CustomUser;
import se.sti.card_school_security.model.dto.CustomUserDetailsDTO;

public class UserDetailsMapper {


    public CustomUserDetailsDTO toUserDetailsDTO(CustomUser customUser) {


        return new CustomUserDetailsDTO(
                customUser.getEmail(),
                customUser.getPassword(),
                customUser.isAccountNonExistent(),
                customUser.isAccountNonLocked(),
                customUser.isAccountNonLocked(),
                customUser.isEnabled(),
                customUser.getRoles());
    }
}
