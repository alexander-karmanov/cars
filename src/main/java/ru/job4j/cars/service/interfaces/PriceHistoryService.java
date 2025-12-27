package ru.job4j.cars.service.interfaces;

import ru.job4j.cars.model.PriceHistory;

import java.util.Set;

public interface PriceHistoryService {

    Set<PriceHistory> getPriceHistoriesByPostId(int id);

}