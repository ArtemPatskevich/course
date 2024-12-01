package main.utils;

import lombok.Data;
import main.models.dto.User;

@Data
public class UserSession {
    private static UserSession instance = null;

    private int id;

    public static UserSession getInstance() {
        if(instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void fillIn(User user) {
        this.id = user.getId();
    }

    public void logOut() {
        this.id = 0;
        instance = null;
    }
}
