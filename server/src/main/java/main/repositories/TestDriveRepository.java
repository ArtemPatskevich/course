package main.repositories;

import main.models.entities.TestDriveEntity;
import org.springframework.data.repository.CrudRepository;

public interface TestDriveRepository extends CrudRepository<TestDriveEntity, Integer> {
}
