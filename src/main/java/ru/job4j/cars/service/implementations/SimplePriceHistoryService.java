package ru.job4j.cars.service.implementations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.interfaces.PriceHistoryRepository;
import ru.job4j.cars.service.interfaces.PriceHistoryService;

import java.util.Set;

@Service
@AllArgsConstructor
public class SimplePriceHistoryService implements PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;

    @Override
    public Set<PriceHistory> getPriceHistoriesByPostId(int id) {
        return priceHistoryRepository.getPriceHistoriesByPostId(id);
    }

}