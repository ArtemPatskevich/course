package main.repositories;

import main.enums.entityAttributes.RoleName;
import main.models.entities.RoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {
    RoleEntity findIdByName(RoleName name);
}
