package by.bsuir.models.dto;

import by.bsuir.enums.entituAttributes.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Role {
    private int id;
    private RoleName rolename;

    public Role(RoleName rolename) {
        this.rolename = rolename;
    }
}
