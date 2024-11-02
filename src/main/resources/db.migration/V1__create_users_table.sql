CREATE TABLE users
(
    id        SERIAL PRIMARY KEY,
    email     VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    name      VARCHAR(255),
    user_role VARCHAR(50),
    enabled   BOOLEAN      NOT NULL,
    CONSTRAINT user_role_check CHECK (user_role IN ('ADMIN', 'USER'))
);