package main.models.entities;

import main.models.dto.Car;
import main.models.dto.Request;
import main.models.dto.Role;
import main.models.dto.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "request")
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "is_approved", columnDefinition = "TINYINT")
    private boolean isApproved;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", insertable = false, updatable = false, nullable = false)
    private CarEntity car;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", insertable = false, updatable = false, nullable = false)
    private UserEntity manager;

    @Column(name = "user_id", columnDefinition = "INT")
    private Integer userId;

    @Column(name = "car_id", columnDefinition = "INT")
    private Integer carId;

    @Column(name = "manager_id", columnDefinition = "INT")
    private Integer managerId;

    public Request toRequest() {
        return new Request(id, isApproved,
                new User(
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getFirstname(),
                    user.getLastname(),
                    new Role(user.getRole().getName())
                ),
                new Car(car.getId(),
                        car.getBrand(),
                        car.getCost(),
                        car.getPetrolType(),
                        car.getBodyType(),
                        car.getImagePath()
                ),
                new User(
                        manager.getId(),
                        manager.getUsername(),
                        manager.getPassword(),
                        manager.getFirstname(),
                        manager.getLastname(),
                        new Role(manager.getRole().getName())
                )
        );
    }
}
