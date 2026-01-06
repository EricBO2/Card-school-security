package se.sti.card_school_security.model;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final CustomUserService customUserService;

    public CustomUserDetailsService(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {

        return customUserService.findByEmail(email)
                .map(CustomUserDetails::new);
    }
}