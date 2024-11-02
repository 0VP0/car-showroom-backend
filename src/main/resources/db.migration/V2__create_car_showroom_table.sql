CREATE TABLE car_showroom
(
    id                             SERIAL PRIMARY KEY,
    name                           VARCHAR(100) NOT NULL,
    commercial_registration_number VARCHAR(10)  NOT NULL UNIQUE,
    manager_name                   VARCHAR(100),
    contact_number                 VARCHAR(15)  NOT NULL,
    address                        VARCHAR(255),
    is_deleted                     BOOLEAN      NOT NULL DEFAULT FALSE,
    user_id                        BIGINT       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
