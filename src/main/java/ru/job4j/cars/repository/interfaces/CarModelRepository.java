package ru.job4j.cars.repository.interfaces;

import ru.job4j.cars.model.CarModel;

import java.util.Collection;

public interface CarModelRepository {

    Collection<CarModel> findAll();

    CarModel getById(int id);

}