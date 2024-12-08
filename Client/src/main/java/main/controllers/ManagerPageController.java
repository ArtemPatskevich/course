package main.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.enums.requests.ClientRequestType;
import main.enums.status.ServerResponseStatus;
import main.models.dto.Request;
import main.models.dto.TestDrive;
import main.models.dto.User;
import main.utils.UserSession;
import main.utils.tcp.ClientRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManagerPageController {
    @FXML
    private AnchorPane carControlPanel;

    @FXML
    private AnchorPane startPanel;

    @FXML
    private AnchorPane workingWithRequestsPanel;

    @FXML
    private AnchorPane checkTestDrivePanel;

    @FXML
    private AnchorPane checkRequestsPanel;

    @FXML
    private Button logOut;

    @FXML
    private Button checkRequestsButton;
    @FXML
    private Button checkCarsButton;

    @FXML
    private Button makeRequestButton;

    @FXML
    private Button workingWithTestDriveButton;

    @FXML
    private TableView<Request> requestsTable;

    @FXML
    private TableView<TestDrive> testDriveTable;

    @FXML
    private TableView<Request> checkRequestsTable;

    @FXML
    private TableColumn<Request, String> firstNameLastNameColumn;
    @FXML
    private TableColumn<Request, String> passportNumberColumn;
    @FXML
    private TableColumn<Request, String> phoneNumberColumn;
    @FXML
    private TableColumn<Request, String> usernameColumn;
    @FXML
    private TableColumn<Request, String> carNameColumn;
    @FXML
    private TableColumn<Request, Void> acceptRequestColumn;
    @FXML
    private TableColumn<Request, Void> rejectRequestColumn;

    @FXML
    private TableColumn<TestDrive, String> firstNameLastNameColumnTest;
    @FXML
    private TableColumn<TestDrive, String> usernameColumnTest;
    @FXML
    private TableColumn<TestDrive, String> carNameColumnTest;
    @FXML
    private TableColumn<TestDrive, String> dateColumnTest;

    @FXML
    private TableColumn<Request, String> firstNameLastNameColumnReqCheck;
    @FXML
    private TableColumn<Request, String> usernameColumnReqCheck;
    @FXML
    private TableColumn<Request, String> carNameColumnReqCheck;
    @FXML
    private TableColumn<Request, String> workerColumnReqCheck;
    @FXML
    private TableColumn<Request, String> statusRequestColumnCheck;
    @FXML
    private TableColumn<Request, String> phoneNumberColumnReqCheck;

    @FXML
    public void initialize() {
        checkRequestsButton.setOnAction(event -> handleCheckRequestsPanel());
        makeRequestButton.setOnAction(event -> handleMakeRequestPanel());
        workingWithTestDriveButton.setOnAction(event -> handleWorkingWithTestDrivePanel());

        initializeCheckRequestsTable();
        initializeMakeRequestTable();
        initializeTestDriveTable();

        logOut.setOnAction(event -> logOut(event));

    }

    private void handleCheckRequestsPanel() {
        hideAllPanels();
        workingWithRequestsPanel.setVisible(true);
        refreshCheckRequestsTable();
    }

    private void handleMakeRequestPanel() {
        hideAllPanels();
        workingWithRequestsPanel.setVisible(true);
        refreshMakeRequestTable();
    }

    private void handleWorkingWithTestDrivePanel() {
        hideAllPanels();
        checkTestDrivePanel.setVisible(true);
        refreshTestDriveTable();
    }

    private void refreshCheckRequestsTable() {
        List<Request> requests = getRequestsFromServer();
        ObservableList<Request> requestObservableList = FXCollections.observableArrayList(requests);

        checkRequestsTable.setItems(requestObservableList);

        System.out.println("All requests added to the table: " + requests);
    }
    private void refreshMakeRequestTable() {
        List<Request> requests = getRequestsFromServer();

        List<Request> filteredRequests = requests.stream()
                .filter(request -> !request.isApproved() && request.getApprovedDate() == null)
                .collect(Collectors.toList());

        ObservableList<Request> requestObservableList = FXCollections.observableArrayList(filteredRequests);

        requestsTable.setItems(requestObservableList);

        System.out.println("Filtered requests added to the table: " + filteredRequests);
    }
    private void refreshTestDriveTable() {

        List<TestDrive> testDrives = getTestDrivesFromServer();
        ObservableList<TestDrive> testDriveObservableList = FXCollections.observableArrayList(testDrives);

        testDriveTable.setItems(testDriveObservableList);

        System.out.println("All test drives added to the table: " + testDrives);
    }
    private void initializeCheckRequestsTable() {
        firstNameLastNameColumnReqCheck.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getClient().getUser();
            String fullName = user.getFirstName() + " " + user.getLastName();
            return new SimpleStringProperty(fullName);
        });

        usernameColumnReqCheck.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient().getUser().getUsername()));

        carNameColumnReqCheck.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCar().getBrand()));
        phoneNumberColumnReqCheck.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient().getPhoneNumber()));

        workerColumnReqCheck.setCellValueFactory(cellData -> {
            User manager = cellData.getValue().getManager();
            String fullName = manager.getFirstName() + " " + manager.getLastName();
            return new SimpleStringProperty(fullName);
        });

        statusRequestColumnCheck.setCellValueFactory(cellData -> {
            Request request = cellData.getValue();
            if (request.getApprovedDate() == null) {
                return new SimpleStringProperty("Ожидание");
            } else {
                return new SimpleStringProperty(request.isApproved() ? "Одобрено" : "Отклонено");
            }
        });
    }
    private void initializeMakeRequestTable() {

        firstNameLastNameColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getClient().getUser();
            String fullName = user.getFirstName() + " " + user.getLastName();
            return new SimpleStringProperty(fullName);
        });

        passportNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient().getPassportNumber()));

        phoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient().getPhoneNumber()));

        usernameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient().getUser().getUsername()));

        carNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCar().getBrand()));
        acceptRequestColumn.setCellFactory(col -> new TableCell<Request, Void>() {
            private final Button btn = new Button("Одобрить");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item != null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(btn);
                    btn.setOnAction(event -> {
                        Request request = getTableView().getItems().get(getIndex());
                        request.setApproved(true);
                        forRequest(request);
                    });
                }
            }
        });

        rejectRequestColumn.setCellFactory(col -> new TableCell<Request, Void>() {
            private final Button btn = new Button("Отклонить");
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item != null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(btn);
                    btn.setOnAction(event -> {
                        Request request = getTableView().getItems().get(getIndex());
                        request.setApproved(false);
                        forRequest(request);
                    });
                }
            }
        });
    }
    private void forRequest(Request request) {
        request.setManager(UserSession.getInstance().getUser());
        request.setApprovedDate(LocalDateTime.now());
        if(sendRequestToServer(request))
        {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Успех");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Статус успешно изменен.");
            successAlert.showAndWait();
            refreshMakeRequestTable();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Ошибка");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Не удалось изменить статус. Попробуйте ещё раз.");
            errorAlert.showAndWait();
        }
    }

    private void initializeTestDriveTable() {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        firstNameLastNameColumnTest.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getUser();
            String fullName = user.getFirstName() + " " + user.getLastName();
            return new SimpleStringProperty(fullName);
        });

        usernameColumnTest.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getUsername()));

        carNameColumnTest.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCar().getBrand()));

        dateColumnTest.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getDate();
            String formattedDate = date.format(dateFormatter);
            return new SimpleStringProperty(formattedDate);
        });
    }

    private List<Request> getRequestsFromServer() {
        try {
            ClientRequest.sendRequestType(ClientRequestType.GET_REQUESTS);
            List<Request> reqs = (List<Request>) ClientRequest.input.readObject();
            System.out.println("here is: " + reqs);
            return reqs;
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private List<TestDrive> getTestDrivesFromServer() {
        try {
            ClientRequest.sendRequestType(ClientRequestType.GET_TEST_DRIVES);
            return (List<TestDrive>) ClientRequest.input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private boolean sendRequestToServer(Request req) {
        try {
            ClientRequest.sendRequestType(ClientRequestType.ADD_REQUEST);
            ClientRequest.output.writeObject(req);
            ServerResponseStatus status = (ServerResponseStatus) ClientRequest.input.readObject();

            return status.equals(ServerResponseStatus.OK);
        } catch (Exception e) {
            return false;
        }
    }

    private void hideAllPanels()
    {
        carControlPanel.setVisible(false);
        startPanel.setVisible(false);
        workingWithRequestsPanel.setVisible(false);
        checkTestDrivePanel.setVisible(false);
        checkRequestsPanel.setVisible(false);;
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
}