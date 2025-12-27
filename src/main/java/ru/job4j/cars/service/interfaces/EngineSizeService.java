package ru.job4j.cars.service.interfaces;

import ru.job4j.cars.model.EngineSize;

import java.util.Collection;

public interface EngineSizeService {

    Collection<EngineSize> findAll();

}