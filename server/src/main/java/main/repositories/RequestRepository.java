package main.repositories;

import main.models.entities.CarEntity;
import main.models.entities.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestRepository extends CrudRepository<RequestEntity, Integer> {
    @Query(value = """
                    SELECT r.car, COUNT(*) AS usage_count
                    FROM RequestEntity r
                    GROUP BY r.carId
                    ORDER BY usage_count DESC
                    LIMIT 3
""")
    List<CarEntity> getTopThreeCars();
}
