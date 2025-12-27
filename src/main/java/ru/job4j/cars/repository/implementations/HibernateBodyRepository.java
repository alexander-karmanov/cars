package ru.job4j.cars.repository.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Body;
import ru.job4j.cars.repository.interfaces.BodyRepository;

import java.util.Collection;

@Repository
@AllArgsConstructor
public class HibernateBodyRepository implements BodyRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все типы кузовов.
     * @return список кузовов.
     */
    @Override
    public Collection<Body> findAll() {
        return crudRepository.query("FROM Body", Body.class);
    }

}