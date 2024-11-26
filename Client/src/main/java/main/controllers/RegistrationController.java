package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.entities.Client;
import main.entities.Role;
import main.entities.User;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

import static main.enums.RoleName.CLIENT;

public class RegistrationController {
    @FXML
    private TextField surname;
    @FXML
    private TextField name;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField passportNumber;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField repeatPassword;
    @FXML
    private DatePicker birthDate;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label passportNumberLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label repeatPasswordLabel;
    @FXML
    private Label birthDateLabel;
    @FXML
    private Button signIn;
    @FXML
    private Button signUp;

    @FXML
    public void initialize() {
        signIn.setOnAction(event -> handlePage(event, "Authorization.fxml", "Authorization",600, 500));
        signUp.setOnAction(this::handleSignUp);
    }

    private void handleSignUp(ActionEvent event) {
        clearErrorLabels();
        boolean isValid = true;

        String surnameText = surname.getText();
        if (surnameText.isEmpty() || !isValidName(surnameText)) {
            surnameLabel.setText("Введите фамилию корректно");
            surnameLabel.setStyle("-fx-text-fill: red;");
            isValid = false;
        }

        String nameText = name.getText();
        if (nameText.isEmpty() || !isValidName(nameText)) {
            nameLabel.setText("Введите имя корректно");
            nameLabel.setStyle("-fx-text-fill: red;");
            isValid = false;
        }

        String phoneText = phoneNumber.getText();
        if (!isValidPhoneNumber(phoneText)) {
            phoneNumberLabel.setText("Введите номер телефона корректно");
            phoneNumberLabel.setStyle("-fx-text-fill: red;");
            isValid = false;
        }

        String passportText = passportNumber.getText();
        if (!isValidPassportNumber(passportText)) {
            passportNumberLabel.setText("Введите номер паспорта корректно");
            passportNumberLabel.setStyle("-fx-text-fill: red;");
            isValid = false;
        }

        String usernameText = username.getText();
        if (usernameText.isEmpty() || !isValidUsername(usernameText)) {
            usernameLabel.setText("Введите имя пользователя корректно");
            usernameLabel.setStyle("-fx-text-fill: red;");
            isValid = false;
        }

        String passwordText = password.getText();
        if (!isValidPassword(passwordText)) {
            passwordLabel.setText("Введите пароль корректно");
            passwordLabel.setStyle("-fx-text-fill: red;");
            isValid = false;
        }

        String repeatPasswordText = repeatPassword.getText();
        if (!passwordText.equals(repeatPasswordText)) {
            repeatPasswordLabel.setText("Пароли не совпадают");
            repeatPasswordLabel.setStyle("-fx-text-fill: red;");
            isValid = false;
        }

        if (!isValidPassword(repeatPasswordText)) {
            repeatPasswordLabel.setText("Введите пароль корректно");
            repeatPasswordLabel.setStyle("-fx-text-fill: red;");
            isValid = false;
        }

        if (birthDate.getValue() == null || !isValidAge(birthDate.getValue())) {
            birthDateLabel.setText("Вы должны быть старше 18 лет");
            birthDateLabel.setStyle("-fx-text-fill: red;");
            isValid = false;
        }

        if (isValid) {
            if (!isUsernameTaken(usernameText)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка регистрации");
                alert.setHeaderText(null);
                alert.setContentText("Имя пользователя уже занято. Пожалуйста, выберите другое имя.");
                alert.showAndWait();

                username.clear();
                return;
            }
            System.out.println("Successful registration!");
            String hashedPassword = hashPassword(passwordText);

            Role role = new Role(CLIENT);
            User user = new User(usernameText, hashedPassword, surnameText, nameText, role);
            Client client = new Client(user,phoneText,passportText,birthDate.getValue());

            handlePage(event, "userPage.fxml", "PaTaaRS_Auto", 800,700);
            clearFields();
        }
    }

    private boolean isValidName(String name) {
        return Pattern.matches("^[А-ЯA-Z][а-яёЁa-zA-Z'-]*$", name);
    }

    private boolean isValidPhoneNumber(String phone) {
        return Pattern.matches("^\\+375(29|33)\\d{7}$", phone);
    }

    private boolean isValidPassportNumber(String passport) {
        return Pattern.matches("^[A-Z]{2}\\d{6}$", passport);
    }

    private boolean isValidUsername(String username) {
        return Pattern.matches("^[a-zA-Z0-9_-]{4,16}$", username);
    }

    private boolean isValidPassword(String password) {
        return Pattern.matches("^[a-zA-Z0-9]{8,16}$", password);
    }

    private boolean isValidAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears() >= 18;
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

    private void clearErrorLabels() {
        surnameLabel.setText("");
        nameLabel.setText("");
        phoneNumberLabel.setText("");
        passportNumberLabel.setText("");
        repeatPasswordLabel.setText("");
        birthDateLabel.setText("");
        usernameLabel.setText("");
        passwordLabel.setText("");
    }

    private void clearFields() {
        surname.clear();
        name.clear();
        phoneNumber.clear();
        passportNumber.clear();
        username.clear();
        password.clear();
        repeatPassword.clear();
        birthDate.getEditor().clear();
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

    private boolean isUsernameTaken(String username) {

        return false;
    }
}