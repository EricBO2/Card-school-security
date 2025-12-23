package se.sti.card_school_security.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import se.sti.card_school_security.model.CustomUser;
import se.sti.card_school_security.model.CustomUserDetails;
import se.sti.card_school_security.model.CustomUserDetailsService;
import se.sti.card_school_security.model.CustomUserRepository;
import se.sti.card_school_security.model.authority.UserRole;
import se.sti.card_school_security.model.dto.AuthTokenResponseDTO;
import se.sti.card_school_security.model.dto.CustomUserLoginDTO;
import se.sti.card_school_security.model.dto.CustomUserRegisterDTO;
import se.sti.card_school_security.model.mapper.UserMapper;
import se.sti.card_school_security.security.jwt.JwtUtils;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthenticationRestController {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final CustomUserRepository customUserRepository;

    @Autowired
    public AuthenticationRestController(CustomUserDetailsService userDetailsService, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, UserMapper userMapper, CustomUserRepository customUserRepository) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.customUserRepository = customUserRepository;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthTokenResponseDTO>> login(@RequestBody Mono<CustomUserLoginDTO> requestMono) {

        return requestMono.flatMap(request ->

                        userDetailsService.findByUsername(request.email()) // Antar att DTO:n har en 'email' metod/fält

                                .flatMap(user -> {
                                    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                                        return Mono.error(new BadCredentialsException("Ogiltig e-post eller lösenord"));
                                    }
                                    return jwtUtils.generateJwtToken(user);
                                })
                )

                // 5. Mappa den genererade strängen (token) till AuthTokenResponseDTO
                .map(token ->ResponseEntity.ok(new AuthTokenResponseDTO(token)));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<AuthTokenResponseDTO>> register(@RequestBody Mono<CustomUserRegisterDTO> requestMono) {

        return requestMono
                .map(dto -> userMapper.registerToCustomUser(
                        dto,
                        Set.of(UserRole.PLAYER),
                        100
                ))
                .flatMap(user -> customUserRepository.save(user))
                .flatMap(savedUser -> {
                    CustomUserDetails userDetails = new CustomUserDetails(savedUser);
                    return jwtUtils.generateJwtToken(userDetails)
                            .map(token ->
                                    ResponseEntity.ok(
                                            new AuthTokenResponseDTO(token)
                                    )
                            );
                })
                .onErrorMap(e ->
                        new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "User registration failed",
                                e
                        )
                );
    }

}
