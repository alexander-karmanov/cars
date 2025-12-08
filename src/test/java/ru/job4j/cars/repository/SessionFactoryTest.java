package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.Image;
import ru.job4j.cars.model.User;

public class SessionFactoryTest {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            try {
                sessionFactory = new Configuration()
                        .configure("hibernate-test.cfg.xml")
                        .addAnnotatedClass(User.class)
                        .addAnnotatedClass(Image.class)
                        .addAnnotatedClass(Engine.class)
                        .buildSessionFactory();
            } catch (Exception e) {
                throw new RuntimeException("Error creating SessionFactory", e);
            }
        }
        return sessionFactory;
    }
}
