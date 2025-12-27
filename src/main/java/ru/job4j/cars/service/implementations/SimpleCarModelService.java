package ru.job4j.cars.service.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.CarModel;
import ru.job4j.cars.repository.interfaces.CarModelRepository;
import ru.job4j.cars.service.interfaces.CarModelService;

import java.util.Collection;

@Service
@AllArgsConstructor
public class SimpleCarModelService implements CarModelService {

    private final CarModelRepository carModelRepository;

    @Override
    public Collection<CarModel> findAll() {
        return carModelRepository.findAll();
    }

    @Override
    public CarModel getById(int id) {
        return carModelRepository.getById(id);
    }

}