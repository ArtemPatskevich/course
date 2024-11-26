package by.bsuir.repositories;

import by.bsuir.models.entities.ClientEntity;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<ClientEntity, Integer> {
}
