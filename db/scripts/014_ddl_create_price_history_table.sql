CREATE TABLE price_history (
    id          SERIAL      PRIMARY KEY,
    price       BIGINT      NOT NULL,
    date        TIMESTAMP   NOT NULL,
    post_id     INT         REFERENCES post(id) ON DELETE CASCADE
);