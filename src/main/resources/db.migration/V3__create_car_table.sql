CREATE TABLE car
(
    id              SERIAL PRIMARY KEY,
    vin             VARCHAR(25) NOT NULL,
    maker           VARCHAR(25) NOT NULL,
    model           VARCHAR(25) NOT NULL,
    model_year      INTEGER     NOT NULL CHECK (model_year >= 0 AND model_year <= 9999),
    price           NUMERIC     NOT NULL,
    is_deleted      BOOLEAN     NOT NULL DEFAULT FALSE,
    car_showroom_id BIGINT      NOT NULL,
    user_id         BIGINT      NOT NULL,
    FOREIGN KEY (car_showroom_id) REFERENCES car_showroom (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
