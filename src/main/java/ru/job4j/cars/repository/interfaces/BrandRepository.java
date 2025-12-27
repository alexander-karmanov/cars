package ru.job4j.cars.repository.interfaces;

import ru.job4j.cars.model.Brand;

import java.util.Collection;

public interface BrandRepository {

    Collection<Brand> findAll();

    Brand getById(int id);

}