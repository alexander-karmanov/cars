CREATE TABLE model (
    id          SERIAL  PRIMARY KEY,
    name        TEXT    NOT NULL,
    brand_id    INT     NOT NULL    REFERENCES brand(id)
);