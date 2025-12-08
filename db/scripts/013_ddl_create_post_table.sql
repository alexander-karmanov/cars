CREATE TABLE post (
    id              SERIAL      PRIMARY KEY,
    description     TEXT        NOT NULL,
    car_id          INT         NOT NULL        UNIQUE REFERENCES car(id),
    created         TIMESTAMP   NOT NULL        DEFAULT now(),
    sold            BOOLEAN     NOT NULL        DEFAULT FALSE,
    price           BIGINT      NOT NULL,
    user_id         INT         NOT NULL        REFERENCES auto_user(id),
    image_id        INT                         REFERENCES image(id),
    UNIQUE (car_id, user_id)
);