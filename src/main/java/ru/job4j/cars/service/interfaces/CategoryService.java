package ru.job4j.cars.service.interfaces;

import ru.job4j.cars.model.Category;

import java.util.Collection;

public interface CategoryService {

    Collection<Category> findAll();

}