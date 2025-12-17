package se.sti.card_school_security.model;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface CustomUserRepository extends R2dbcRepository<CustomUser, UUID> {

    public Mono<CustomUser> findUserByEmail(String email);
}
