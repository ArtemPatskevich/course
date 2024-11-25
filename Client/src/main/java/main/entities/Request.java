package main.entities;

public class Request {
    private int id;
    private boolean isApproved;
    private User user;
    private Car car;
    private User manager;

    Request() {}

    public Request(boolean isApproved, User user, Car car, User manager) {
        this.isApproved = isApproved;
        this.user = user;
        this.car = car;
        this.manager = manager;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
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

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }
}
