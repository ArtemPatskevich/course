package main.Strategy;

import main.models.dto.Car;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DescendingCostSortStrategy implements SortStrategy {
    @Override
    public List<Car> sort(List<Car> cars) {
        return cars.stream()
                .sorted(Comparator.comparingDouble(Car::getCost).reversed())
                .collect(Collectors.toList());
    }
}
