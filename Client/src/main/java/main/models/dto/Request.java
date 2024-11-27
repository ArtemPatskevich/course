package main.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Request implements Serializable {
    private int id;
    private boolean isApproved;
    private User user;
    private Car car;
    private User manager;
    private LocalDateTime sendDate;
    private LocalDateTime approvedDate;

    public Request(boolean isApproved, User user, Car car, User manager, LocalDateTime sendDate, LocalDateTime approvedDate) {
        this.isApproved = isApproved;
        this.user = user;
        this.car = car;
        this.manager = manager;
        this.sendDate = sendDate;
        this.approvedDate = approvedDate;
    }
}
