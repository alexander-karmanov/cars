CREATE TABLE engine (
    id              SERIAL  PRIMARY KEY,
    fuel_type_id    INT     NOT NULL        REFERENCES fuel_type(id),
    engine_size_id  INT     NOT NULL        REFERENCES engine_size(id)
);