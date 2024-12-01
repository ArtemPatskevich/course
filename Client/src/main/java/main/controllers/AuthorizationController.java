package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.enums.entityAttributes.RoleName;
import main.enums.requests.ClientRequestType;
import main.models.dto.User;
import main.utils.UserSession;
import main.utils.tcp.ClientRequest;

import java.io.IOException;
import java.util.regex.Pattern;

public class AuthorizationController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(this::handleLogin);
        registerButton.setOnAction(event -> handlePage(event, "Registration.fxml","Registration", 600,500, false, false));
    }

    private void handleLogin(ActionEvent event) {
        clearErrorLabels();

        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = authorizeUser(new User(username, password, null, null, null));

        if (user == null) {
            usernameLabel.setText("Неверное имя пользователя или пароль");
            usernameLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        UserSession.getInstance().fillIn(user);
        RoleName userRole = user.getRole().getRolename();
        switch (userRole) {
            case CLIENT:
                handlePage(event, "userPage.fxml", "PaTaaRS_Auto", 800,700, false, true);
                break;
            case ADMIN:
                handlePage(event, "adminPage.fxml", "PaTaaRS_Auto", 800,700, false, true);
                break;
            case MANAGER:
                handlePage(event, "managerPage.fxml", "PaTaaRS_Auto", 800,700, false, true);
                break;
            default:
                System.out.println("Unknown role");
                break;
        }

        clearFields();
    }

    private void clearErrorLabels() {
        usernameLabel.setText("");
        passwordLabel.setText("");
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

    private void handlePage(ActionEvent event, String page, String title, int width, int height, boolean isResizable, boolean isMaximized) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/" + page));
            Scene scene = new Scene(loader.load(), width, height);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(isResizable);
            stage.setMaximized(isMaximized);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User authorizeUser(User user) {
        User authorizedUser = null;
        try {
            ClientRequest.sendRequestType(ClientRequestType.AUTHORIZE_USER);
            ClientRequest.output.writeObject(user);
            authorizedUser = (User) ClientRequest.input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return authorizedUser;
    }
}