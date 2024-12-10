package main.Strategy;

import main.models.dto.Car;

import java.util.List;

public interface SortStrategy {
    List<Car> sort(List<Car> cars);
}
