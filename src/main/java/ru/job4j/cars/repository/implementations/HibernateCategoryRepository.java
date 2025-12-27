package ru.job4j.cars.repository.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Category;
import ru.job4j.cars.repository.interfaces.CategoryRepository;

import java.util.Collection;

@Repository
@AllArgsConstructor
public class HibernateCategoryRepository implements CategoryRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все типы категорий.
     * @return список категорий.
     */
    @Override
    public Collection<Category> findAll() {
        return crudRepository.query("FROM Category", Category.class);
    }

}