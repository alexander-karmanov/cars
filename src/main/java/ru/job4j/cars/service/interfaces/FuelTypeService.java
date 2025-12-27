package ru.job4j.cars.service.interfaces;

import ru.job4j.cars.model.FuelType;

import java.util.Collection;

public interface FuelTypeService {

    Collection<FuelType> findAll();

}