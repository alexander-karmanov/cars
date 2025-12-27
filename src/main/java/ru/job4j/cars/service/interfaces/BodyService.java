package ru.job4j.cars.service.interfaces;

import ru.job4j.cars.model.Body;

import java.util.Collection;

public interface BodyService {

    Collection<Body> findAll();

}