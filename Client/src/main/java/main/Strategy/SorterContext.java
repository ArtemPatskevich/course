package main.Strategy;

import main.models.dto.Car;

import java.util.List;

public class SorterContext {
    private SortStrategy sortStrategy;

    public SorterContext(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public List<Car> executeSort(List<Car> cars) {
        return sortStrategy.sort(cars);
    }
}
