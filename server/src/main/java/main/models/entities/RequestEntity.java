package main.models.entities;

import main.models.dto.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "client_id", insertable = false, updatable = false, nullable = false)
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", insertable = false, updatable = false, nullable = false)
    private CarEntity car;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", insertable = false, updatable = false, nullable = false)
    private UserEntity manager;

    @Column(name = "client_id", columnDefinition = "INT")
    private Integer clientId;

    @Column(name = "car_id", columnDefinition = "INT")
    private Integer carId;

    @Column(name = "manager_id", columnDefinition = "INT")
    private Integer managerId;

    @Column(name = "send_date", columnDefinition = "DATETIME")
    private LocalDateTime sendDate;

    @Column(name = "approve_date", columnDefinition = "DATETIME")
    private LocalDateTime approvedDate;

    public Request toRequest() {
        return new Request(id, isApproved,
                new Client(
                    client.getId(),
                    client.getUser().toUser(),
                    client.getPhoneNumber(),
                    client.getPassportNumber(),
                    client.getBirthDate()
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
                ),
                sendDate, approvedDate
        );
    }
}
