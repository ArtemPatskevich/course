package main.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TestDrive implements Serializable {
    private int id;
    private User user;
    private Car car;
    private LocalDateTime date;

    public TestDrive(User user, Car car, LocalDateTime date) {
        this.user = user;
        this.car = car;
        this.date = date;
    }
}
