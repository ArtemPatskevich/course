package main.entities;

import java.time.LocalDateTime;

public class TestDrive {
    private int id;
    private User user;
    private Car car;
    private LocalDateTime date;

    TestDrive(){}

    TestDrive(User user, Car car, LocalDateTime date)
    {
        this.user = user;
        this.car = car;
        this.date = date;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
