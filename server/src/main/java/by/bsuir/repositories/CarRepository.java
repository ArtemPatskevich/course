package by.bsuir.repositories;

import by.bsuir.models.entities.CarEntity;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<CarEntity, Integer> {
}
