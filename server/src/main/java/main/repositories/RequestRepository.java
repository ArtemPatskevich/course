package main.repositories;

import main.models.entities.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<RequestEntity, Integer> {
}
