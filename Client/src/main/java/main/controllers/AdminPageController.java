package main.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import main.enums.entityAttributes.BodyType;
import main.enums.entityAttributes.PetrolType;
import main.enums.entityAttributes.RoleName;
import main.enums.requests.ClientRequestType;
import main.enums.status.ServerResponseStatus;
import main.models.dto.*;
import main.utils.UserSession;
import main.utils.tcp.ClientRequest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminPageController {

    @FXML
    private AnchorPane carControlPanel;

    @FXML
    private AnchorPane startPanel;

    @FXML
    private ComboBox<String> carControl;
    @FXML
    private ComboBox<String> userControl;
    @FXML
    private AnchorPane checkUserPanel;
    @FXML
    private AnchorPane deleteUserPanel;
    @FXML
    private Button makingReport;

    @FXML
    private Button logOut;

    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableView<User> usersTableDelete;
    @FXML
    private TableColumn<User, String> firstNameColumnDel;
    @FXML
    private TableColumn<User, String> lastNameColumnDel;
    @FXML
    private TableColumn<User, String> roleColumnDel;
    @FXML
    private TableColumn<User, String> usernameColumnDel;
    @FXML
    private TableColumn<User, Void> deleteColumn;

    @FXML
    private AnchorPane deleteCarPanel;
    @FXML
    private TableView<Car> carsTableDelete;
    @FXML
    private TableColumn<Car, String> brandColumnDel;
    @FXML
    private TableColumn<Car, String> priceColumnDel;
    @FXML
    private TableColumn<Car, String> carTypeColumnDel;
    @FXML
    private TableColumn<Car, String> fuelTypeColumnDel;
    @FXML
    private TableColumn<Car, Void> deleteCarsColumn;

    @FXML
    private AnchorPane updateCarPanel;
    @FXML
    private TableView<Car> carsTableUpdate;
    @FXML
    private TableColumn<Car, String> brandColumnUpd;
    @FXML
    private TableColumn<Car, Double> priceColumnUpd;
    @FXML
    private TableColumn<Car, String> carTypeColumnUpd;
    @FXML
    private TableColumn<Car, String> fuelTypeColumnUpd;
    @FXML
    private TableColumn<Car, Void> CarsColumnUpd;

    @FXML
    public void initialize() {
        makingReport.setOnAction(event -> generateReport());
        logOut.setOnAction(event -> logOut(event));
        setupUserControlListener();
        initializeTableColumns();
        initializeDeleteTableColumns();
        initializeDeleteCarsTableColumns();
        initializeUpdateTableColumns();
        setupCarControlListener();
    }
    private void setupUserControlListener() {
        userControl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleUserControlSelection(newValue);
        });
    }
    private void setupCarControlListener() {
        carControl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleCarControlSelection(newValue);
        });
    }
    private void initializeTableColumns() {
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole().getRolename().toString()));
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
    }

    private void initializeDeleteTableColumns() {
        firstNameColumnDel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumnDel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        roleColumnDel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole().getRolename().toString()));
        usernameColumnDel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        deleteColumn.setCellFactory(col -> new TableCell<User, Void>() {
            private final Button deleteButton = new Button("Удалить");
            {
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    deleteUserOnServer(user);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }
    private void initializeDeleteCarsTableColumns() {
        brandColumnDel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBrand()));
        priceColumnDel.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCost())));
        carTypeColumnDel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBodyType().toString()));
        fuelTypeColumnDel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPetrolType().toString()));

        deleteCarsColumn.setCellFactory(col -> new TableCell<Car, Void>() {
            private final Button deleteButton = new Button("Удалить");

            {
                deleteButton.setOnAction(event -> {
                    Car car = getTableView().getItems().get(getIndex());
                    boolean isDeleted = deleteCarOnServer(car);
                    if(isDeleted)
                    {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Успех");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Автомобиль успешно удалён.");
                        successAlert.showAndWait();
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Ошибка");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Не удалось удалить автомобиль. Попробуйте ещё раз.");
                        errorAlert.showAndWait();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }
    private void initializeUpdateTableColumns() {
        brandColumnUpd.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBrand()));
        priceColumnUpd.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getCost()).asObject()
        );
        carTypeColumnUpd.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBodyType().toString()));
        fuelTypeColumnUpd.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPetrolType().toString()));

        brandColumnUpd.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumnUpd.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        carTypeColumnUpd.setCellFactory(TextFieldTableCell.forTableColumn());
        fuelTypeColumnUpd.setCellFactory(TextFieldTableCell.forTableColumn());

        brandColumnUpd.setOnEditCommit(event -> {
            Car car = event.getRowValue();
            car.setBrand(event.getNewValue());
        });

        priceColumnUpd.setOnEditCommit(event -> {
            Car car = event.getRowValue();
            car.setCost(event.getNewValue());
        });

        carTypeColumnUpd.setOnEditCommit(event -> {
            Car car = event.getRowValue();
            BodyType newBodyType = BodyType.valueOf(event.getNewValue().toUpperCase());
            car.setBodyType(newBodyType);
        });

        fuelTypeColumnUpd.setOnEditCommit(event -> {
            Car car = event.getRowValue();
            PetrolType newPetrolType = PetrolType.valueOf(event.getNewValue().toUpperCase());
            car.setPetrolType(newPetrolType);
        });

        CarsColumnUpd.setCellFactory(col -> new TableCell<Car, Void>() {
            private final Button updateButton = new Button("Обновить");
            {
                updateButton.setOnAction(event -> {
                    Car car = getTableView().getItems().get(getIndex());
                    updateCar(car);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });

        carsTableUpdate.setEditable(true);
    }
    private boolean deleteCarOnServer(Car car)
    {
        return true;
    }
    private void deleteUserOnServer(User user) {
        try {
            ClientRequest.sendRequestType(ClientRequestType.DELETE_USER);
            ClientRequest.output.writeObject(user.getId());
            ServerResponseStatus status = (ServerResponseStatus) ClientRequest.input.readObject();

            //TODO
            // success/error alert
            if(status.equals(ServerResponseStatus.OK)) {
            } else {

            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void addUsersTable(List<User> users) {
        ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        usersTable.setItems(observableUsers);
    }
    private void addUsersToDeleteTable(List<User> users) {
        ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        usersTableDelete.setItems(observableUsers);
    }
    private void addCarsToDeleteTable(List<Car> cars) {
        ObservableList<Car> observableCars = FXCollections.observableArrayList(cars);
        carsTableDelete.setItems(observableCars);
    }
    private void addCarsToUpdateTable(List<Car> cars) {
        ObservableList<Car> observableCars = FXCollections.observableArrayList(cars);
        carsTableUpdate.setItems(observableCars);
    }

    private List<User> getUsersFromServer() {
        try {
            ClientRequest.sendRequestType(ClientRequestType.GET_USERS);
            return (List<User>) ClientRequest.input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Car> getCarsFromServer()
    {
        return new ArrayList<>();
    }

    private void handleUserControlSelection(String selectedValue) {
        if (selectedValue != null) {
            switch (selectedValue) {
                case "Просмотреть пользователей":
                    closePanels();
                    checkUserPanel.setVisible(true);
                    addUsersTable(getUsersFromServer());
                    break;
                case "Убрать пользователя":
                    closePanels();
                    deleteUserPanel.setVisible(true);
                    addUsersToDeleteTable(getUsersFromServer());
                    break;
            }
        }
    }

    private void handleCarControlSelection(String selectedValue) {
        if (selectedValue != null) {
            switch (selectedValue) {
                case "Просмотреть автомобили":
                    closePanels();
                    break;
                case "Удалить автомобили":
                    closePanels();
                    deleteCarPanel.setVisible(true);
                    addCarsToDeleteTable(getCarsFromServer());
                    break;
                case "Добавить автомобиль":
                    closePanels();
                    break;
                case "Изменить автомобиль":
                    closePanels();
                    updateCarPanel.setVisible(true);
                    addCarsToUpdateTable(getCarsFromServer());
                    break;
                default:
                    break;
            }
        }
    }

    private void updateCar(Car car) {
        System.out.println("Обновление автомобиля: " + car);
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
                        request.getClient().getUser().getFirstName(),
                        request.getClient().getUser().getLastName());
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

    private List<Request> getRequestsFromServer() {
        try {
            ClientRequest.sendRequestType(ClientRequestType.GET_REQUESTS);
            return (List<Request>) ClientRequest.input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void closePanels() {
        startPanel.setVisible(false);
        carControlPanel.setVisible(false);
        checkUserPanel.setVisible(false);
        deleteUserPanel.setVisible(false);
        deleteCarPanel.setVisible(false);
    }
}