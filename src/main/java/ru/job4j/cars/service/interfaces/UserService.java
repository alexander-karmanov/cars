package ru.job4j.cars.service.interfaces;

import ru.job4j.cars.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> save(User user);

    boolean update(User user);

    Optional<User> findByEmailAndPassword(String email, String password);

    void deleteByEmailAndPassword(String email, String password);

}