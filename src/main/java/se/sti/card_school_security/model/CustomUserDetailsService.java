package se.sti.card_school_security.model;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.sti.card_school_security.model.dto.CustomUserDetailsDTO;
import se.sti.card_school_security.model.mapper.UserDetailsMapper;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomUserRepository customUserRepository;
    private final UserDetailsMapper userDetailsMapper;

    public CustomUserDetailsService(CustomUserRepository customUserRepository, UserDetailsMapper userDetailsMapper) {
        this.customUserRepository = customUserRepository;
        this.userDetailsMapper = userDetailsMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomUserDetailsDTO customUserDetailsDTO = userDetailsMapper.toUserDetailsDTO( customUserRepository.findUserByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("User %s not found", email))
                ));
        return new CustomUserDetails(customUserDetailsDTO);
    }
}
