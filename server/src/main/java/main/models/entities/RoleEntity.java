package main.models.entities;

import lombok.*;
import main.enums.entityAttributes.RoleName;
import main.models.dto.Role;
import jakarta.persistence.*;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", columnDefinition = "ENUM('CLIENT','ADMIN','MANAGER')")
    private RoleName name;

    public Role toRole() {
        return new Role(id, name);
    }
}
