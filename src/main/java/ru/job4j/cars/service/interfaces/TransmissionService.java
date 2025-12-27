package ru.job4j.cars.service.interfaces;

import ru.job4j.cars.model.Transmission;

import java.util.Collection;

public interface TransmissionService {

    Collection<Transmission> findAll();

}