package ru.job4j.cars.repository.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Repository
@Slf4j
@AllArgsConstructor
public class CrudRepository {

    private final SessionFactory sf;

    public boolean run(Consumer<Session> command) {
        try {
            tx(session -> {
                command.accept(session);
                return null;
            });
            return true;
        } catch (RuntimeException e) {
            if (e.getMessage().equals("DUPLICATE_ENTRY")) {
                log.warn("Ошибка: запись уже существует (дубликат)");
            } else {
                log.error("Ошибка БД: {}", e.getMessage());
            }
            return false;
        } catch (Exception e) {
            log.error("Неожиданная ошибка: {}", e.getMessage());
            return false;
        }
    }

    public boolean run(String query, Map<String, Object> args) {
        Consumer<Session> command = session -> {
            var sq = session.createQuery(query);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            sq.executeUpdate();
        };
        return run(command);
    }

    public <T> Optional<T> optional(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, Optional<T>> command = session -> {
            var sq = session.createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.uniqueResultOptional();
        };
        return tx(command);
    }

    public <T> T one(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, T> command = session -> {
            var sq = session.createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.uniqueResult();
        };
        return tx(command);
    }

    public <T> List<T> query(String query, Class<T> cl) {
        Function<Session, List<T>> command = session -> session
                .createQuery(query, cl)
                .list();
        return tx(command);
    }

    public <T> List<T> query(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, List<T>> command = session -> {
            var sq = session.createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.list();
        };
        return tx(command);
    }

    public <T> T tx(Function<Session, T> command) {
        Session session = sf.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            T rsl = command.apply(session);
            transaction.commit();
            return rsl;
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (isDuplicateError(e)) {
                throw new RuntimeException("DUPLICATE_ENTRY", e);
            } else {
                throw e;
            }
        } finally {
            session.close();
        }
    }

    private boolean isDuplicateError(PersistenceException e) {
        String msg = e.getMessage().toLowerCase();
        return msg.contains("duplicate")
                || msg.contains("unique")
                || msg.contains("constraint");
    }
}
