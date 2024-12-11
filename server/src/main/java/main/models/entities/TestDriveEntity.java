package main.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.models.dto.TestDrive;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
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
    private Integer userId;

    @Column(name = "car_id", columnDefinition = "INT")
    private Integer carId;

    @Column(name = "date", columnDefinition = "DATETIME")
    private LocalDateTime date;

    public TestDriveEntity(TestDrive testDrive) {
        this.id = testDrive.getId();
        this.user = new UserEntity(testDrive.getUser());
        this.car = new CarEntity(testDrive.getCar());
        this.userId = testDrive.getUser().getId();
        this.carId = testDrive.getCar().getId();
        this.date = testDrive.getDate();
    }

    public TestDrive toTestDrive() {
        return new TestDrive(id, user.toUser(), car.toCar(), date);
    }
}
