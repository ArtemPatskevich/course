package main.models.entities;

import main.models.dto.Client;
import main.models.dto.Role;
import main.models.dto.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

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
