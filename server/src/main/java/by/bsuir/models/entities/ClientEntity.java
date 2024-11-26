package by.bsuir.models.entities;

import by.bsuir.models.dto.Client;
import by.bsuir.models.dto.Role;
import by.bsuir.models.dto.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@Table(name = "client")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true, insertable = false, updatable = false, nullable = false)
    private UserEntity user;

    @Column(name = "user_id", columnDefinition = "INT")
    private Integer userId;

    @Column(name = "phone_number", columnDefinition = "VARCHAR(15)", unique = true)
    private String phoneNumber;

    @Column(name = "passport_number", columnDefinition = "VARCHAR(15)", unique = true)
    private String passportNumber;

    @Column(name = "birth_date", columnDefinition = "DATE")
    private LocalDate birthDate;

    public Client toClient() {
        return new Client(id, new User(
                                user.getId(),
                                user.getUsername(),
                                user.getPassword(),
                                user.getFirstname(),
                                user.getLastname(),
                                new Role(user.getRole().getName())
                            ),
                phoneNumber, passportNumber, birthDate);
    }
}
