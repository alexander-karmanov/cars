package ru.job4j.cars.service.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Color;
import ru.job4j.cars.repository.interfaces.ColorRepository;
import ru.job4j.cars.service.interfaces.ColorService;

import java.util.Collection;

@Service
@AllArgsConstructor
public class SimpleColorService implements ColorService {

    private final ColorRepository colorRepository;

    @Override
    public Collection<Color> findAll() {
        return colorRepository.findAll();
    }

}