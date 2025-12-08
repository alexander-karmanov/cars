package ru.job4j.cars.repository;

import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.User;

@Repository
@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        Optional<User> result = Optional.empty();
        if (crudRepository.run(session -> session.save(user))) {
            result = Optional.of(user);
        }
        return result;
    }

    @Override
    public boolean update(User user) {
        return crudRepository.run(session -> session.update(user));
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return crudRepository.optional(
                "FROM User WHERE email = :uEmail AND password = :uPassword",
                User.class,
                Map.of("uEmail", email, "uPassword", password)
        );
    }

    @Override
    public void deleteByEmailAndPassword(String email, String password) {
        crudRepository.run("DELETE FROM User WHERE email = :uEmail AND password = :uPassword",
                Map.of("uEmail", email, "uPassword", password)
        );
    }
}
