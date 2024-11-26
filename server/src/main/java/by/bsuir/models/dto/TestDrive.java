package by.bsuir.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TestDrive {
    private int id;
    private User user;
    private Car car;
    private LocalDateTime date;

    TestDrive(User user, Car car, LocalDateTime date) {
        this.user = user;
        this.car = car;
        this.date = date;
    }
}
