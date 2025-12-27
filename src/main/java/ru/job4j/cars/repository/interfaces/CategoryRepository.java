package ru.job4j.cars.repository.interfaces;

import ru.job4j.cars.model.Category;

import java.util.Collection;

public interface CategoryRepository {

    Collection<Category> findAll();

}