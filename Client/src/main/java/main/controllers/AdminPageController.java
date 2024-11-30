package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.models.dto.Request;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminPageController {

    @FXML
    private AnchorPane carControlPanel;

    @FXML
    private AnchorPane startPanel;

    @FXML
    private AnchorPane userControlPanel;

    @FXML
    private ComboBox<String> carControl;

    @FXML
    private ComboBox<String> userControl;

    @FXML
    private Button makingReport;

    @FXML
    private Button logOut;

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        makingReport.setOnAction(event -> generateReport());
        logOut.setOnAction(event -> logOut(event));
    }

    private void generateReport() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение формирования отчета");
        alert.setHeaderText("Вы уверены, что хотите сформировать отчет?");
        alert.setContentText("Нажмите OK, чтобы подтвердить.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Формирование отчета...");

                if (makingReport(getRequestsFromServer())) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Успех");
                        successAlert.setHeaderText("Отчет успешно сформирован");
                        successAlert.setContentText("Отчет был записан в файл.");
                        successAlert.showAndWait();
                } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Ошибка");
                        errorAlert.setHeaderText("Не удалось сформировать отчет");
                        errorAlert.setContentText("Произошла ошибка при записи отчета в файл.");
                        errorAlert.showAndWait();
                }

            } else {
                System.out.println("Формирование отчета отменено.");
            }
        });
    }

    private boolean makingReport(List<Request> requests) {
        String filePath = "report.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Request request : requests) {
                String fullNameUser = String.format("%s %s",
                        request.getUser().getFirstName(),
                        request.getUser().getLastName());
                String fullNameManager = String.format("%s %s",
                        request.getManager().getFirstName(),
                        request.getManager().getLastName());
                String line = String.format("ID: %d, Approved: %b, User: %s, Car: %s, Manager: %s, Send Date: %s, Approved Date: %s",
                        request.getId(),
                        request.isApproved(),
                        fullNameUser,
                        request.getCar().getBrand(),
                        fullNameManager,
                        request.getSendDate(),
                        request.getApprovedDate());

                writer.write(line);
                writer.newLine();
            }
            System.out.println("Отчет успешно сформирован");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при записи отчета: " + e.getMessage());
            return false;
        }
    }

    private void logOut(javafx.event.ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение выхода");
        alert.setHeaderText("Вы уверены, что хотите выйти?");
        alert.setContentText("Нажмите OK, чтобы подтвердить выход.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
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

    private List<Request> getRequestsFromServer() {
        return new ArrayList<>();
    }

    public void showCarControlPanel() {
        startPanel.setVisible(false);
        carControlPanel.setVisible(true);
        userControlPanel.setVisible(false);
    }

    public void showUserControlPanel() {
        startPanel.setVisible(false);
        carControlPanel.setVisible(false);
        userControlPanel.setVisible(true);
    }
}