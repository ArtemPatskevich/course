package main.entities;

import main.enums.RoleName;

public class Role {
    private int id;
    private RoleName rolename;

    Role(){}

    public Role(RoleName rolename) {
        this.rolename = rolename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoleName getRoleName() {
        return rolename;
    }

    public void setRoleName(RoleName rolename) {
        this.rolename = rolename;
    }
}
