package main.repositories;

import main.models.entities.CarEntity;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<CarEntity, Integer> {
}
