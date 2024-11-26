package by.bsuir.models.entities;

import by.bsuir.enums.RoleName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", columnDefinition = "ENUM('CLIENT','ADMIN','MANAGER')")
    private RoleName name;
}
