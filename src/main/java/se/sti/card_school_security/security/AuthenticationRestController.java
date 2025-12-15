package se.sti.card_school_security.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import se.sti.card_school_security.model.CustomUserDetails;
import se.sti.card_school_security.model.dto.CustomUserLoginDTO;
import se.sti.card_school_security.model.mapper.UserMapper;
import se.sti.card_school_security.security.jwt.JwtUtils;

import java.util.Map;
@RestController
public class AuthenticationRestController {

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final JwtUtils jwtUtils;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AuthenticationRestController(ReactiveAuthenticationManager reactiveAuthenticationManager,
                                        JwtUtils jwtUtils) {
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> authenticateUser(
            @RequestBody CustomUserLoginDTO dto) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        return Mono.fromCallable(() -> authenticationManager.authenticate(authToken))
                .subscribeOn(Schedulers.boundedElastic()) // <-- gör den icke-blockerande
                .flatMap(authentication -> {

                    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                    String token = jwtUtils.generateJwtToken(userDetails);

                    // Cookie i WebFlux
                    ResponseCookie cookie = ResponseCookie.from("authToken", token)
                            .httpOnly(true)
                            .secure(false) // true i produktion
                            .path("/")
                            .maxAge(Duration.ofHours(1))
                            .sameSite("Lax")
                            .build();

                    Map<String, Object> body = Map.of(
                            "username", dto.email(),
                            "authorities", userDetails.getAuthorities(),
                            "token", token
                    );

                    return Mono.just(ResponseEntity
                            .ok()
                            .header(HttpHeaders.SET_COOKIE, cookie.toString())
                            .body(body));
                })
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
                );
    }

}
