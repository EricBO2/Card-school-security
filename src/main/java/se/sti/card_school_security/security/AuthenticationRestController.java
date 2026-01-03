package se.sti.card_school_security.security;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import se.sti.card_school_security.model.AuthService;
import se.sti.card_school_security.model.dto.AuthTokenResponseDTO;
import se.sti.card_school_security.model.dto.CustomUserLoginDTO;
import se.sti.card_school_security.model.dto.CustomUserRegisterDTO;

@RestController
@RequestMapping("/auth")
public class AuthenticationRestController {

    private final AuthService authService;

    public AuthenticationRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthTokenResponseDTO>> login(
            @RequestBody Mono<CustomUserLoginDTO> request
    ) {
        return request
                .flatMap(authService::login)
                .map(token -> ResponseEntity.ok(new AuthTokenResponseDTO(token)));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<AuthTokenResponseDTO>> register(
            @RequestBody Mono<CustomUserRegisterDTO> request
    ) {
        return request
                .flatMap(authService::register)
                .map(token -> ResponseEntity.ok(new AuthTokenResponseDTO(token)));
    }
}
