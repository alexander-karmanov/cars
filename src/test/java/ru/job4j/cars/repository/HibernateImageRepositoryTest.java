package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import ru.job4j.cars.model.Image;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class HibernateImageRepositoryTest {

    private static SessionFactory sessionFactory;
    private CrudRepository crudRepository;
    private HibernateImageRepository imageRepository;

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
        imageRepository = new HibernateImageRepository(crudRepository);

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("delete from Image").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void whenFindByIdThenSuccess() {
        int testId;
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            Image image = new Image();
            session.save(image);
            session.getTransaction().commit();
            testId = image.getId();
        }
        Optional<Image> foundImage = imageRepository.findById(testId);
        assertTrue(foundImage.isPresent());
        assertEquals(testId, foundImage.get().getId());
    }

    @Test
    void whenFindByIdWithNonExistentIdThenEmpty() {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long count = (Long) session.createQuery("SELECT COUNT(*) FROM Image").uniqueResult();
            assertEquals(0L, count);
            session.getTransaction().commit();
        }
        int nonExistentId = 999;
        Optional<Image> foundImage = imageRepository.findById(nonExistentId);
        assertTrue(foundImage.isEmpty());
    }
}
