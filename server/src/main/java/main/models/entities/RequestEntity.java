package main.models.entities;

import lombok.NoArgsConstructor;
import main.models.dto.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "request")
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "is_approved", columnDefinition = "TINYINT")
    private Boolean isApproved;

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

    public RequestEntity(Request request) {
        this.id = request.getId();
        this.isApproved = request.isApproved();
        this.clientId = request.getClient().getId();
        this.carId = request.getCar().getId();
        if(request.getManager() == null){
            this.managerId = null;
        } else {
            this.managerId = request.getManager().getId();
        }
        this.sendDate = request.getSendDate();
        this.approvedDate = request.getApprovedDate();
    }

    public Request toRequest() {
        User manager = null;
        if(this.manager != null) {
            manager = new User(
                    this.manager.getId(),
                    this.manager.getUsername(),
                    this.manager.getPassword(),
                    this.manager.getFirstname(),
                    this.manager.getLastname(),
                    this.manager.getRole().toRole()
            );
        }
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
                manager,
                sendDate, approvedDate
        );
    }
}
