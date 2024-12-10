package main.models.entities;

import jakarta.persistence.*;
import lombok.*;
import main.models.dto.Role;
import main.models.dto.User;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username", columnDefinition = "VARCHAR(50)")
    private String username;

    @Column(name = "password", columnDefinition = "VARCHAR(255)")
    private String password;

    @Column(name = "firstname", columnDefinition = "VARCHAR(50)")
    private String firstname;

    @Column(name = "lastname", columnDefinition = "VARCHAR(50)")
    private String lastname;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", insertable = false, updatable = false, nullable = false)
    private RoleEntity role;

    @Column(name = "role_id", columnDefinition = "INT")
    private Integer roleId;

    public UserEntity(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstname = user.getFirstName();
        this.lastname = user.getLastName();
        this.role = new RoleEntity(user.getRole().getId(), user.getRole().getRolename());
        this.roleId = user.getRole().getId();
    }

    public User toUser() {
        return new User(id, username, password, firstname, lastname, new Role(role.getName()));
    }
}
