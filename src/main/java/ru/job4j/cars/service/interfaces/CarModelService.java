package ru.job4j.cars.service.interfaces;

import ru.job4j.cars.model.CarModel;

import java.util.Collection;

public interface CarModelService {

    Collection<CarModel> findAll();

    CarModel getById(int id);

}