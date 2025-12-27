package ru.job4j.cars.repository.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.FuelType;
import ru.job4j.cars.repository.interfaces.FuelTypeRepository;

import java.util.Collection;

@Repository
@AllArgsConstructor
public class HibernateFuelTypeRepository implements FuelTypeRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все типы топлива.
     * @return список типов топлива.
     */
    @Override
    public Collection<FuelType> findAll() {
        return crudRepository.query("FROM FuelType", FuelType.class);
    }

}