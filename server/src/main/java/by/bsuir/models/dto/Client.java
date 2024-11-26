package by.bsuir.models.dto;

import by.bsuir.models.entities.ClientEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class Client {
    private int id;
    private User user;
    private String phoneNumber;
    private String passportNumber;
    private LocalDate birthDate;

    public Client(User user, String phoneNumber, String passportNumber, LocalDate birthDate) {
        this.user = user;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
        this.birthDate = birthDate;
    }

    public ClientEntity toClientEntity(int userId) {
        return new ClientEntity(null, null, userId, phoneNumber, passportNumber, birthDate);
    }
}
