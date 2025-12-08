package ru.job4j.cars.repository;

import org.junit.jupiter.api.*;
import org.hibernate.SessionFactory;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.EngineSize;
import ru.job4j.cars.model.FuelType;

import static org.junit.jupiter.api.Assertions.*;

public class HibernateEngineRepositoryTest {

    private static SessionFactory sessionFactory;

    private CrudRepository crudRepository;
    private EngineRepository engineRepository;

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
        engineRepository = new HibernateEngineRepository(crudRepository);

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("delete from Engine").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void whenSaveEngineThenSuccess() {
        FuelType fuelType = new FuelType();
        fuelType.setName("Petrol");
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(fuelType);
            session.getTransaction().commit();
        }

        EngineSize engineSize = new EngineSize();
        engineSize.setSize(2);
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(engineSize);
            session.getTransaction().commit();
        }
        Engine engine = new Engine();
        engine.setFuelType(fuelType);
        engine.setEngineSize(engineSize);
        Engine savedEngine = engineRepository.save(engine);

        assertNotNull(savedEngine);
        assertTrue(savedEngine.getId() > 0);

        try (var session = sessionFactory.openSession()) {
            Engine dbEngine = session.get(Engine.class, savedEngine.getId());
            assertNotNull(dbEngine);
            assertEquals("Petrol", dbEngine.getFuelType().getName());
            assertEquals(2, dbEngine.getEngineSize().getSize());
        }
    }
}
