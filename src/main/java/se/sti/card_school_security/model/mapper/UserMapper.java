package se.sti.card_school_security.model.mapper;

import org.springframework.stereotype.Component;
import se.sti.card_school_security.model.CustomUser;
import se.sti.card_school_security.model.authority.UserRole;
import se.sti.card_school_security.model.dto.CustomUserRegisterDTO;

import java.util.Set;

@Component
public class UserMapper {
    public CustomUser registerToCustomUser(CustomUserRegisterDTO registerDTO, Set<UserRole> userRole, int startScore) {
        return new CustomUser(
                startScore,
                registerDTO.getUsername(),
                registerDTO.getEmail(),
                registerDTO.getPassword(),
                true,
                true,
                true,
                true,
                userRole
        );
    }
}
