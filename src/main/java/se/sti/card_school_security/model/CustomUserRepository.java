package se.sti.card_school_security.model;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface CustomUserRepository extends R2dbcRepository<CustomUser, UUID> {

}
