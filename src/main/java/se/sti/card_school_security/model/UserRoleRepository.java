package se.sti.card_school_security.model;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface UserRoleRepository extends ReactiveCrudRepository<UserRoleEntity, Void> {

    Flux<UserRoleEntity> findByUserId(UUID userId);
}
