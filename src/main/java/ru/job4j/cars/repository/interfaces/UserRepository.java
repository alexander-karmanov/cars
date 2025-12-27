package ru.job4j.cars.repository.interfaces;

import ru.job4j.cars.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> save(User user);

    boolean update(User user);

    Optional<User> findByEmailAndPassword(String email, String password);

    void deleteByEmailAndPassword(String email, String password);

}