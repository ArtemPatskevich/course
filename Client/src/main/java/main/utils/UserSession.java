package main.utils;

import lombok.Data;
import main.models.dto.User;

@Data
public class UserSession {
    private static UserSession instance = null;

    private User user;

    public static UserSession getInstance() {
        if(instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void fillIn(User user) {
        this.user = user;
    }

    public void logOut() {
        this.user = null;
        instance = null;
    }
}
