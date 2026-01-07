package se.sti.card_school_security.security;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import se.sti.card_school_security.model.AuthService;
import se.sti.card_school_security.model.CustomUserDetails;
import se.sti.card_school_security.model.dto.AuthTokenResponseDTO;
import se.sti.card_school_security.model.dto.CustomUserLoginDTO;
import se.sti.card_school_security.model.dto.CustomUserRegisterDTO;
import se.sti.card_school_security.model.dto.PasswordDTO;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
public class AuthenticationRestController {

    private final AuthService authService;

    public AuthenticationRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthTokenResponseDTO>> login(
            @RequestBody Mono<CustomUserLoginDTO> request,
            ServerHttpResponse response
    ) {
        return request
                .flatMap(authService::login)
                .map(token -> {
                    ResponseCookie cookie = ResponseCookie.from("token", token)
                            .httpOnly(true)
                            .path("/")
                            .maxAge(Duration.ofMinutes(10))
                            .build();
                    response.addCookie(cookie);
                    return ResponseEntity.ok(new AuthTokenResponseDTO(token));
                });
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(ServerHttpResponse response) {

        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addCookie(cookie);

        return Mono.just(ResponseEntity.ok().build());
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<AuthTokenResponseDTO>> register(
            @RequestBody Mono<CustomUserRegisterDTO> request,
            ServerHttpResponse response
    ) {

        return request
                .flatMap(authService::register)
                .map(token -> {
                    ResponseCookie cookie = ResponseCookie.from("token", token)
                            .httpOnly(true)
                            .path("/")
                            .maxAge(Duration.ofMinutes(10))
                            .build();

                    response.addCookie(cookie);
                    return ResponseEntity.ok(new AuthTokenResponseDTO(token));

                });
    }

    @PostMapping("/user/delete")
    public Mono<ResponseEntity<Void>> deleteSelf(
            @AuthenticationPrincipal CustomUserDetails user, //hämtar cutomUserDetails från token
            @RequestBody PasswordDTO dto,
            ServerHttpResponse response
    ) {
        return authService
                .deleteSelf(user.getUsername(), dto.getPassword())
                .thenReturn(ResponseEntity.noContent().build());
    }


    @GetMapping("/player-only")
    public Mono<ResponseEntity<String>> playerOnly() {
        return Mono.just(ResponseEntity.ok().build());
    }

    @GetMapping("/admin-only")
    public Mono<ResponseEntity<String>> adminOnly() {
        return Mono.just(ResponseEntity.ok().build());
    }


}
