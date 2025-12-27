package ru.job4j.cars.service.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Transmission;
import ru.job4j.cars.repository.interfaces.TransmissionRepository;
import ru.job4j.cars.service.interfaces.TransmissionService;

import java.util.Collection;

@Service
@AllArgsConstructor
public class SimpleTransmissionService implements TransmissionService {

    private final TransmissionRepository transmissionRepository;

    @Override
    public Collection<Transmission> findAll() {
        return transmissionRepository.findAll();
    }

}