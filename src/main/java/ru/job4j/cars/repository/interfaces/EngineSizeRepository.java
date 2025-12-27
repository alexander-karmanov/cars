package ru.job4j.cars.repository.interfaces;

import ru.job4j.cars.model.EngineSize;

import java.util.Collection;

public interface EngineSizeRepository {

    Collection<EngineSize> findAll();

}