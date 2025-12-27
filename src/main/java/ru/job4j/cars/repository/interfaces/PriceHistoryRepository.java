package ru.job4j.cars.repository.interfaces;

import ru.job4j.cars.model.PriceHistory;

import java.util.Set;

public interface PriceHistoryRepository {

    Set<PriceHistory> getPriceHistoriesByPostId(int id);

}