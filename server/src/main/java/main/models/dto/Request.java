package main.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Request implements Serializable {
    private int id;
    private boolean isApproved;
    private Client client;
    private Car car;
    private User manager;
    private LocalDateTime sendDate;
    private LocalDateTime approvedDate;

    public Request(boolean isApproved, Client client, Car car, User manager, LocalDateTime sendDate, LocalDateTime approvedDate) {
        this.isApproved = isApproved;
        this.client = client;
        this.car = car;
        this.manager = manager;
        this.sendDate = sendDate;
        this.approvedDate = approvedDate;
    }
}
