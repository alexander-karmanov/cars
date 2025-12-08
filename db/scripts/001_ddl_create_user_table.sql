CREATE TABLE auto_user (
    id          SERIAL      PRIMARY KEY,
    email       TEXT        NOT NULL UNIQUE,
    password    TEXT        NOT NULL,
    name        TEXT        NOT NULL,
    phone       TEXT        NOT NULL UNIQUE
);