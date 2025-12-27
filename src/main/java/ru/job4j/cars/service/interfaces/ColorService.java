package ru.job4j.cars.service.interfaces;

import ru.job4j.cars.model.Color;

import java.util.Collection;

public interface ColorService {

    Collection<Color> findAll();

}