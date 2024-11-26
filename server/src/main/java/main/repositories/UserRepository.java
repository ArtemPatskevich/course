package main.repositories;

import main.models.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findByUsernameAndPassword(String username, String password);
    UserEntity findByUsername(String username);
    boolean existsByUsername(String username);
}
