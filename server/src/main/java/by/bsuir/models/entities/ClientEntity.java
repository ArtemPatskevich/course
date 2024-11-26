package by.bsuir.models.entities;

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

}
