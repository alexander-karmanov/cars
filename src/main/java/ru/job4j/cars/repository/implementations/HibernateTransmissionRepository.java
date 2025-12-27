package ru.job4j.cars.repository.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Transmission;
import ru.job4j.cars.repository.interfaces.TransmissionRepository;

import java.util.Collection;

@Repository
@AllArgsConstructor
public class HibernateTransmissionRepository implements TransmissionRepository {

    private final CrudRepository crudRepository;

    /**
     * Получить из БД все виды трансмиссий.
     * @return список трансмиссий.
     */
    @Override
    public Collection<Transmission> findAll() {
        return crudRepository.query("FROM Transmission", Transmission.class);
    }

}