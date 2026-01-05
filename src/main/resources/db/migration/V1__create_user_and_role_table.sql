CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE app_user
(
    id                         UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    username                   VARCHAR(25)  NOT NULL UNIQUE,
    email                      VARCHAR(254) NOT NULL UNIQUE,
    password                   VARCHAR(255) NOT NULL,
    score                      INTEGER      NOT NULL,

    is_account_non_existent    BOOLEAN      NOT NULL DEFAULT TRUE,
    is_account_non_locked      BOOLEAN      NOT NULL DEFAULT TRUE,
    is_credentials_non_expired BOOLEAN      NOT NULL DEFAULT TRUE,
    is_enabled                 BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE user_roles
(
    user_id UUID        NOT NULL,
    role    VARCHAR(50) NOT NULL,

    PRIMARY KEY (user_id, role),
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES app_user (id)
            ON DELETE CASCADE
);

