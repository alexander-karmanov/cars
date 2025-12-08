package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

    public class HibernateUserRepositoryTest {
        private static SessionFactory sessionFactory;
        private CrudRepository crudRepository;
        private HibernateUserRepository userRepository;

        @BeforeAll
        static void init() {
            sessionFactory = SessionFactoryTest.getSessionFactory();
        }

        @AfterAll
        static void close() {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }

        @BeforeEach
        void setUp() {
            crudRepository = new CrudRepository(sessionFactory);
                    userRepository = new HibernateUserRepository(crudRepository);
            var session = sessionFactory.openSession();
            session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            session.getTransaction().commit();
            session.close();
        }

        @Test
        void whenSaveUserThenReturnUserWithId() {
        var user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setName("name");
        user.setPhone("phone");
        var result = userRepository.save(user);
        assertTrue(result.isPresent());
        var savedUser = result.get();
        assertNotNull(savedUser.getId());
        var session = sessionFactory.openSession();
        var dbUser = session.get(User.class, savedUser.getId());
        assertNotNull(dbUser);
        assertEquals("test@example.com", dbUser.getEmail());
        session.close();
    }

    @Test
    void whenUpdateUserThenSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }

        user.setEmail("person@example.com");
        assertTrue(userRepository.update(user));
        try (Session session = sessionFactory.openSession()) {
            User user1 = session.get(User.class, user.getId());
            assertEquals("person@example.com", user1.getEmail());
        }
    }

    @Test
    void whenFindUserByEmailAndPasswordThenSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("secret");
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
        Optional<User> result = userRepository.findByEmailAndPassword("test@example.com", "secret");
        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
        Optional<User> emptyResult = userRepository.findByEmailAndPassword("wrong@example.com", "wrong");
        assertFalse(emptyResult.isPresent());
    }

    @Test
    void whenDeleteUserByEmailAndPasswordThenSuccess() {
        User user = new User();
        user.setEmail("delete@example.com");
        user.setPassword("deletePass");
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
        userRepository.deleteByEmailAndPassword("delete@example.com", "deletePass");
        try (Session session = sessionFactory.openSession()) {
            User dbUser = session.get(User.class, user.getId());
            assertNull(dbUser);
        }
    }
}
