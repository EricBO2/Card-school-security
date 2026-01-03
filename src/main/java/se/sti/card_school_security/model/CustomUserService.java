package se.sti.card_school_security.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.sti.card_school_security.model.authority.UserRole;
import se.sti.card_school_security.model.dto.CustomUserRegisterDTO;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserService {


    private final UserRoleRepository userRoleRepository;
    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserService(UserRoleRepository userRoleRepository, CustomUserRepository customUserRepository, PasswordEncoder passwordEncoder) {
        this.userRoleRepository = userRoleRepository;
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;
    }


    Mono<CustomUser> findByEmail(String email) {
        return customUserRepository.findUserByEmail(email)
                .switchIfEmpty(Mono.error(
                        new UsernameNotFoundException("User not found: " + email)
                ))
                .flatMap(user ->
                        userRoleRepository.findByUserId(user.getId())
                                .map(UserRoleEntity::getRole)
                                .collect(Collectors.toSet())
                                .map(roles -> {
                                    user.setRoles(roles);
                                    return user;
                                })
                );
    }

    public Mono<CustomUser> register(CustomUserRegisterDTO dto) {

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        Set<UserRole> roles = Set.of(UserRole.PLAYER);

        CustomUser user = CustomUser.create(dto, roles);
        user.setPassword(hashedPassword);

        return customUserRepository.save(user)
                .flatMap(savedUser ->
                        Flux.fromIterable(roles)
                                .map(role -> new UserRoleEntity(
                                        savedUser.getId(),
                                        role
                                ))
                                .flatMap(userRoleRepository::save)
                                .then(Mono.just(savedUser))
                );
    }
}
