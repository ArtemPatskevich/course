package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.enums.requests.ClientRequestType;
import main.enums.status.ServerResponseStatus;
import main.models.dto.Car;
import main.models.dto.Request;
import main.models.dto.TestDrive;
import main.utils.UserSession;
import main.utils.tcp.ClientRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserPageController {
    @FXML
    private AnchorPane startPanel;
    @FXML
    private AnchorPane carControlPanel;
    @FXML
    private AnchorPane signUpToTestDrivePanel;
    @FXML
    private AnchorPane checkMyRequestsPanel;
    @FXML
    private AnchorPane checkMyTestDrivesPanel;

    @FXML
    private Button logOut;

    @FXML
    private VBox carsContainer;

    @FXML
    private ComboBox<String> checkCarBox;
    @FXML
    private ComboBox<String> workWithTestDrivesBox;
    @FXML
    private Button checkRequests;
    @FXML
    private ComboBox<String> chooseCar;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField timeField;
    @FXML
    private Button signUpToTestDriveButton;
    @FXML
    public void initialize() {
        signUpToTestDriveButton.setOnAction(event -> handleSignUpToTestDrive());
        logOut.setOnAction(event -> logOut(event));
        setupCarControlListener();
        setupTestDriveControlListener();
    }

    private void setupCarControlListener() {
        checkCarBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleCarSelection(newValue);
        });
    }
    private void handleCarSelection(String selectedValue) {
        if (selectedValue != null) {
            switch (selectedValue) {
                case "От самого дорогого к самому дешевому":
                    closePanels();
                    carControlPanel.setVisible(true);
                    break;
                case "От самого дешевого к самому дорогому":
                    closePanels();
                    carControlPanel.setVisible(true);
                    break;
                case "Сортировка по типу топлива":
                    closePanels();
                    carControlPanel.setVisible(true);
                    break;
            }
        }
    }

    private void setupTestDriveControlListener() {
        workWithTestDrivesBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleTestDriveSelection(newValue);
        });
    }
    private void handleTestDriveSelection(String selectedValue) {
        if (selectedValue != null) {
            switch (selectedValue) {
                case "Просмотр тест-драйвов":
                    closePanels();
                    checkMyTestDrivesPanel.setVisible(true);
                    break;
                case "Запись на тест-драйв":
                    closePanels();
                    loadCarNames();
                    signUpToTestDrivePanel.setVisible(true);
                    break;
            }
        }
    }

    private void handleSignUpToTestDrive() {
        String selectedCarName = chooseCar.getValue();
        LocalDate selectedDate = dateField.getValue();
        String timeInput = timeField.getText();

        Car selectedCar = getCarByName(selectedCarName);

        if (!isValidTimeFormat(timeInput)) {
            showAlert("Неверное время","Неверный формат времени.");
            timeField.clear();
            return;
        }

        if (selectedDate == null || !isDateValid(selectedDate)) {
            showAlert("Неверная дата","Выберите дату, которая не может быть ранее текущей.");
            dateField.setValue(null);
            return;
        }
        String[] timeParts = timeInput.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        LocalDateTime dateTime = selectedDate.atTime(hour, minute);

        TestDrive test = new TestDrive(UserSession.getInstance().getUser(),selectedCar,dateTime);
        boolean isSended = sendTestDriveToServer(test);
        if(isSended)
        {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Успех");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Вы успешно записаны.");
            successAlert.showAndWait();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Ошибка");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Не удалось записаться. Попробуйте ещё раз.");
            errorAlert.showAndWait();
        }
        clearFields();
    }
    private boolean isValidTimeFormat(String time) {
        return time.matches("\\d{2}:\\d{2}");
    }

    private boolean isDateValid(LocalDate date) {
        return !date.isBefore(LocalDate.now());
    }

    private void clearFields() {
        chooseCar.getSelectionModel().clearSelection();
        dateField.setValue(null);
        timeField.clear();
    }

    //ToDo
    private List<Car> getCarsFromServer() {
        try {
            ClientRequest.sendRequestType(ClientRequestType.GET_CARS);
            return (List<Car>) ClientRequest.input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    //ToDo
    private boolean sendTestDriveToServer(TestDrive test) {
        try {
            ClientRequest.sendRequestType(ClientRequestType.ADD_TEST_DRIVE);
            ClientRequest.output.writeObject(test);

            ServerResponseStatus status = (ServerResponseStatus) ClientRequest.input.readObject();
            return status.equals(ServerResponseStatus.OK);
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }


    public void closePanels() {
        startPanel.setVisible(false);
        carControlPanel.setVisible(false);
        signUpToTestDrivePanel.setVisible(false);
        checkMyRequestsPanel.setVisible(false);
        checkMyTestDrivesPanel.setVisible(false);
    }
    private void loadCarNames() {
        List<Car> cars = getCarsFromServer();
        List<String> carNames = new ArrayList<>();
        for (Car car : cars) {
            carNames.add(car.getBrand());
        }
        chooseCar.getItems().addAll(carNames);
    }
    private void logOut(javafx.event.ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение выхода");
        alert.setHeaderText("Вы уверены, что хотите выйти?");
        alert.setContentText("Нажмите OK, чтобы подтвердить выход.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                UserSession.getInstance().logOut();

                System.out.println("Выход из системы...");
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/main/Authorization.fxml"));
                    Scene scene = new Scene(root);

                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.setTitle("Authorization");
                    window.sizeToScene();
                    window.centerOnScreen();
                    window.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Выход отменен.");
            }
        });
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private Car getCarByName(String name) {
        List<Car> cars = getCarsFromServer();
        for (Car car : cars) {
            if (car.getBrand().equals(name)) {
                return car;
            }
        }
        return null;
    }
}
