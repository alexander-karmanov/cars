package ru.job4j.cars.service.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.interfaces.CarRepository;
import ru.job4j.cars.service.interfaces.CarService;

@Service
@AllArgsConstructor
public class SimpleCarService implements CarService {

    private final CarRepository carRepository;

    @Override
    public void delete(Car car) {
        carRepository.delete(car);
    }

}