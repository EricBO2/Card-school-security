package se.sti.card_school_security.model;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import se.sti.card_school_security.model.dto.CustomUserLoginDTO;
import se.sti.card_school_security.model.dto.CustomUserRegisterDTO;
import se.sti.card_school_security.security.jwt.JwtUtils;

@Service
public class AuthService {
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CustomUserService customUserService;

    public AuthService(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, CustomUserService customUserService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.customUserService = customUserService;
    }

    public Mono<String> login(CustomUserLoginDTO dto) {
        return userDetailsService.findByUsername(dto.email())
                .flatMap(user -> {
                    if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
                        return Mono.error(
                                new BadCredentialsException("Ogiltig e-post eller lösenord")
                        );
                    }
                    return jwtUtils.generateJwtToken(user);
                });
    }

    public Mono<String> register(CustomUserRegisterDTO dto) {
        return customUserService.register(dto)
                .map(CustomUserDetails::new)
                .flatMap(jwtUtils::generateJwtToken);
    }

}
