package ru.job4j.cars.repository.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Color;
import ru.job4j.cars.repository.interfaces.ColorRepository;

import java.util.Collection;

@Repository
@AllArgsConstructor
public class HibernateColorRepository implements ColorRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все цвета кузовов.
     * @return список цветов.
     */
    @Override
    public Collection<Color> findAll() {
        return crudRepository.query("FROM Color", Color.class);
    }

}