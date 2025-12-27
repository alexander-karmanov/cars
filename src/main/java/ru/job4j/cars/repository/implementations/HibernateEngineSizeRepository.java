package ru.job4j.cars.repository.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.EngineSize;
import ru.job4j.cars.repository.interfaces.EngineSizeRepository;

import java.util.Collection;

@Repository
@AllArgsConstructor
public class HibernateEngineSizeRepository implements EngineSizeRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все объёмы двигателя.
     * @return список объёмов двигателя.
     */
    @Override
    public Collection<EngineSize> findAll() {
        return crudRepository.query("FROM EngineSize", EngineSize.class);
    }

}