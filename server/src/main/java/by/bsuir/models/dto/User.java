package by.bsuir.models.dto;

import by.bsuir.models.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;

    User(String username, String password, String firstName, String lastName, Role role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public UserEntity toUserEntity() {
        return new UserEntity(null, username, password, firstName, lastName, null, role.getId());
    }
}
