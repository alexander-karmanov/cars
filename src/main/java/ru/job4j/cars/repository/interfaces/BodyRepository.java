package ru.job4j.cars.repository.interfaces;

import ru.job4j.cars.model.Body;

import java.util.Collection;

public interface BodyRepository {

    Collection<Body> findAll();

}