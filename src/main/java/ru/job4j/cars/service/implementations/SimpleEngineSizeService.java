package ru.job4j.cars.service.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.EngineSize;
import ru.job4j.cars.repository.interfaces.EngineSizeRepository;
import ru.job4j.cars.service.interfaces.EngineSizeService;

import java.util.Collection;

@Service
@AllArgsConstructor
public class SimpleEngineSizeService implements EngineSizeService {

    private final EngineSizeRepository engineSizeRepository;

    @Override
    public Collection<EngineSize> findAll() {
        return engineSizeRepository.findAll();
    }

}