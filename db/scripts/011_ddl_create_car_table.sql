CREATE TABLE car (
    id              SERIAL  PRIMARY KEY,
    brand_id        INT     NOT NULL        REFERENCES brand(id),
    model_id        INT     NOT NULL        REFERENCES model(id),
    body_id         INT     NOT NULL        REFERENCES body(id),
    engine_id       INT     NOT NULL        REFERENCES engine(id),
    transmission_id INT     NOT NULL        REFERENCES transmission(id),
    color_id        INT     NOT NULL        REFERENCES color(id),
    category_id     INT     NOT NULL        REFERENCES category(id),
    year            INT     NOT NULL,
    mileage         INT     NOT NULL,
    vin             TEXT    NOT NULL
);