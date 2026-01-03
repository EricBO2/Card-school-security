package se.sti.card_school_security.model;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CustomUserRepository extends R2dbcRepository<CustomUser, UUID> {
    public Mono<CustomUser> findUserByEmail(String email);
}
