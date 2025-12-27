package ru.job4j.cars.repository.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.interfaces.CarRepository;

@Repository
@AllArgsConstructor
public class HibernateCarRepository implements CarRepository {

    private final CrudRepository crudRepository;

    /**
     * Удалить автомобиль из БД.
     * @param car автомобиль.
     */
    @Override
    public void delete(Car car) {
        crudRepository.run(session -> session.delete(car));
    }

}