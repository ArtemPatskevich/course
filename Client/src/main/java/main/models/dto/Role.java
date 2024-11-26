package main.models.dto;

import main.enums.entityAttributes.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Role implements Serializable {
    private int id;
    private RoleName rolename;

    public Role(RoleName rolename) {
        this.rolename = rolename;
    }
}
