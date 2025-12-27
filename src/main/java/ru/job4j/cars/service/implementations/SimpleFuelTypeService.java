package ru.job4j.cars.service.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.FuelType;
import ru.job4j.cars.repository.interfaces.FuelTypeRepository;
import ru.job4j.cars.service.interfaces.FuelTypeService;

import java.util.Collection;

@Service
@AllArgsConstructor
public class SimpleFuelTypeService implements FuelTypeService {

    private final FuelTypeRepository fuelTypeRepository;

    @Override
    public Collection<FuelType> findAll() {
        return fuelTypeRepository.findAll();
    }

}