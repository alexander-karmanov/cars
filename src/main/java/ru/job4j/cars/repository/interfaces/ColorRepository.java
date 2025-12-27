package ru.job4j.cars.repository.interfaces;

import ru.job4j.cars.model.Color;

import java.util.Collection;

public interface ColorRepository {

    Collection<Color> findAll();

}