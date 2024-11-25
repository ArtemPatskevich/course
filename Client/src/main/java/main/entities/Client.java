package main.entities;

import java.time.LocalDate;

public class Client {
    private int id;
    private User user;
    private String phoneNumber;
    private String passportNumber;
    private LocalDate birthDate;

    Client() {}

    public Client(User user, String phoneNumber, String passportNumber, LocalDate birthDate) {
        this.user = user;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
        this.birthDate = birthDate;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
