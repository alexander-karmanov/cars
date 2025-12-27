package ru.job4j.cars.repository.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.CarModel;
import ru.job4j.cars.repository.interfaces.CarModelRepository;

import java.util.Collection;
import java.util.Map;

@Repository
@AllArgsConstructor
public class HibernateCarModelRepository implements CarModelRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все модели автомобилей.
     * @return список моделей.
     */
    @Override
    public Collection<CarModel> findAll() {
        return crudRepository.query("FROM CarModel", CarModel.class);
    }

    /**
     * Получить модель автомобиля по ID.
     * @param id модели автомобиля.
     * @return модель автомобиля.
     */
    @Override
    public CarModel getById(int id) {
        return crudRepository.one("FROM CarModel WHERE id = :cId", CarModel.class, Map.of("cId", id));
    }

}