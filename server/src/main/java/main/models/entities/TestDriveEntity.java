package main.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.models.dto.TestDrive;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "test_drive")
public class TestDriveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", insertable = false, updatable = false, nullable = false)
    private CarEntity car;

    @Column(name = "user_id", columnDefinition = "INT")
    private Integer uaerId;

    @Column(name = "car_id", columnDefinition = "INT")
    private Integer carId;

    @Column(name = "date", columnDefinition = "DATETIME")
    private LocalDateTime date;

    public TestDrive toTestDrive() {
        return new TestDrive(id, user.toUser(), car.toCar(), date);
    }
}
