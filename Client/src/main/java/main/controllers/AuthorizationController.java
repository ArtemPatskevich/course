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
        registerButton.setOnAction(this::handleRegister);
    }

    private void handleLogin(ActionEvent event) {
        clearErrorLabels();

        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean isUsernameValid = validateUsername(username);
        boolean isPasswordValid = validatePassword(password);

        if (isUsernameValid && isPasswordValid) {
            System.out.println("Successful sign in!");
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

    private void handleRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Registration.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
}
