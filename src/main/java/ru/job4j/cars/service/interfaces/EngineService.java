package ru.job4j.cars.service.interfaces;

import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.EngineSize;
import ru.job4j.cars.model.FuelType;

import java.util.Optional;

public interface EngineService {

    Engine save(Engine engine);

    Optional<Engine> findByFuelTypeAndSize(FuelType fuelType, EngineSize size);

}