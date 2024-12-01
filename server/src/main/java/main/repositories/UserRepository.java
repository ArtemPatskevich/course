package main.repositories;

import main.enums.entityAttributes.RoleName;
import main.models.entities.RoleEntity;
import main.models.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    @Query(value = """
                SELECT u
                FROM UserEntity u
                WHERE u.role.name = ?1
                """)
    List<UserEntity> findAllByRole(RoleName role);
    UserEntity findByUsernameAndPassword(String username, String password);
    UserEntity findByUsername(String username);
    boolean existsByUsername(String username);
}
