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
import main.entities.User;
import main.enums.RoleName;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        registerButton.setOnAction(event -> handlePage(event, "Registration.fxml","Registration", 600,500));
    }

    private void handleLogin(ActionEvent event) {
        clearErrorLabels();

        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean isUsernameValid = validateUsername(username);
        boolean isPasswordValid = validatePassword(password);

        if (isUsernameValid && isPasswordValid) {
            System.out.println("Successful sign in!");

            User user = new User(username,hashPassword(password));

            RoleName userRole = RoleName.CLIENT;

            switch (userRole) {
                case CLIENT:
                    handlePage(event, "userPage.fxml", "PaTaaRS_Auto", 800,700);
                    break;
                case ADMIN:
                    handlePage(event, "adminPage.fxml", "PaTaaRS_Auto", 800,700);
                    break;
                case MANAGER:
                    handlePage(event, "managerPage.fxml", "PaTaaRS_Auto", 800,700);
                    break;
                default:
                    System.out.println("Unknown role");
                    break;
            }

            clearFields();
        } else {
            if (!isUsernameValid) {
                usernameLabel.setText("Некорректное имя пользователя");
                usernameLabel.setStyle("-fx-text-fill: red;");
            } else {
                usernameLabel.setText("");
            }

            if (!isPasswordValid) {
                passwordLabel.setText("Некорректный пароль");
                passwordLabel.setStyle("-fx-text-fill: red;");
            } else {
                passwordLabel.setText("");
            }
        }
    }

    private boolean validateUsername(String username) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{8,16}$");
        return pattern.matcher(username).matches();
    }

    private boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{8,16}$");
        return pattern.matcher(password).matches();
    }

    private void clearErrorLabels() {
        usernameLabel.setText("");
        passwordLabel.setText("");
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

    private void handlePage(ActionEvent event, String page, String title, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/" + page));
            Scene scene = new Scene(loader.load(), width, height);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}