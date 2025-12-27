package ru.job4j.cars.service.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Body;
import ru.job4j.cars.repository.interfaces.BodyRepository;
import ru.job4j.cars.service.interfaces.BodyService;

import java.util.Collection;

@Service
@AllArgsConstructor
public class SimpleBodyService implements BodyService {

    private final BodyRepository bodyRepository;

    @Override
    public Collection<Body> findAll() {
        return bodyRepository.findAll();
    }

}