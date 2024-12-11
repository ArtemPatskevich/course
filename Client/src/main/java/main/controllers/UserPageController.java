package main.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.Strategy.AscendingCostSortStrategy;
import main.Strategy.DescendingCostSortStrategy;
import main.Strategy.PetrolTypeSortStrategy;
import main.Strategy.SorterContext;
import main.enums.requests.ClientRequestType;
import main.enums.status.ServerResponseStatus;
import main.models.dto.*;
import main.utils.UserSession;
import main.utils.tcp.ClientRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private TableView<TestDrive> testDriveTable;
    @FXML
    private TableColumn<TestDrive, String> firstNameLastNameColumnTest;
    @FXML
    private TableColumn<TestDrive, String> usernameColumnTest;
    @FXML
    private TableColumn<TestDrive, String> carNameColumnTest;
    @FXML
    private TableColumn<TestDrive, String> dateColumnTest;
    @FXML
    private TableColumn<TestDrive, Void> deleteColumnTest;

    @FXML
    private TableView<Request> checkRequestsTable;
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
        checkRequests.setOnAction(event ->handleCheckRequestsPanel());
        logOut.setOnAction(event -> logOut(event));
        setupCarControlListener();
        setupTestDriveControlListener();
        initializeTestDriveTable();
        initializeCheckRequestsTable();
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
            if(manager == null) {
                return new SimpleStringProperty("-");
            }
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

        deleteColumnTest.setCellFactory(col -> new TableCell<TestDrive, Void>() {
            private final Button deleteButton = new Button("Удалить");
            {
                deleteButton.setOnAction(event -> {
                    TestDrive test = getTableView().getItems().get(getIndex());
                    boolean isDeleted = deleteTestDriveOnServer(test);
                    if(isDeleted)
                    {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Успех");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Вы успешно удалили тест-драйв.");
                        successAlert.showAndWait();
                        refreshTestDriveTable();
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Ошибка");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Не удалось удалить. Попробуйте ещё раз.");
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

    private void setupCarControlListener() {
        checkCarBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleCarSelection(newValue);
        });
    }
    private void handleCarSelection(String selectedValue) {
        List<Car> cars = getCarsFromServer();
        SorterContext sorterContext;
        if (selectedValue != null) {
            switch (selectedValue) {
                case "Просмотр авто":
                    closePanels();
                    carControlPanel.setVisible(true);
                    displayCars(cars);
                    break;
                case "От самого дорогого к самому дешевому":
                    closePanels();
                    carControlPanel.setVisible(true);
                    sorterContext = new SorterContext(new DescendingCostSortStrategy());
                    List<Car> sortedDescending = sorterContext.executeSort(new ArrayList<>(cars));
                    displayCars(sortedDescending);
                    break;
                case "От самого дешевого к самому дорогому":
                    closePanels();
                    carControlPanel.setVisible(true);
                    sorterContext = new SorterContext(new AscendingCostSortStrategy());
                    List<Car> sortedAscending = sorterContext.executeSort(new ArrayList<>(cars));
                    displayCars(sortedAscending);
                    break;
                case "Сортировка по типу топлива":
                    closePanels();
                    carControlPanel.setVisible(true);
                    sorterContext = new SorterContext(new PetrolTypeSortStrategy());
                    List<Car> sortedByPetrolType = sorterContext.executeSort(new ArrayList<>(cars));
                    displayCars(sortedByPetrolType);
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
                    refreshTestDriveTable();
                    break;
                case "Запись на тест-драйв":
                    closePanels();
                    loadCarNames();
                    signUpToTestDrivePanel.setVisible(true);
                    break;
            }
        }
    }

    private void handleCheckRequestsPanel() {
        closePanels();
        checkMyRequestsPanel.setVisible(true);
        refreshCheckRequestsTable();
    }
    private void refreshCheckRequestsTable() {
        List<Request> requests = getRequestsFromServer();
        int currentUserId = UserSession.getInstance().getUser().getId();

        List<Request> filteredRequests = requests.stream()
                .filter(request -> request.getClient().getUser().getId() == currentUserId)
                .collect(Collectors.toList());

        ObservableList<Request> requestObservableList = FXCollections.observableArrayList(filteredRequests);

        checkRequestsTable.setItems(requestObservableList);

        System.out.println("Filtered requests added to the table: " + filteredRequests);
    }
    private void refreshTestDriveTable() {
        List<TestDrive> testDrives = getTestDrivesFromServer();
        int currentUserId = UserSession.getInstance().getUser().getId();

        List<TestDrive> filteredTestDrives = testDrives.stream()
                .filter(testDrive -> testDrive.getUser().getId() == currentUserId)
                .collect(Collectors.toList());

        ObservableList<TestDrive> testDriveObservableList = FXCollections.observableArrayList(filteredTestDrives);

        testDriveTable.setItems(testDriveObservableList);

        System.out.println("Filtered test drives added to the table: " + filteredTestDrives);
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

    private List<Car> getCarsFromServer() {
        try {
            ClientRequest.sendRequestType(ClientRequestType.GET_CARS);
            return (List<Car>) ClientRequest.input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
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
    private List<TestDrive> getTestDrivesFromServer() {
        try {
            ClientRequest.sendRequestType(ClientRequestType.GET_TEST_DRIVES);
            return (List<TestDrive>) ClientRequest.input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private List<Request> getRequestsFromServer() {
        try {
            ClientRequest.sendRequestType(ClientRequestType.GET_REQUESTS);
            List<Request> reqs = (List<Request>) ClientRequest.input.readObject();
            return reqs;
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    //ToDo
    private boolean makeRequestOnServer(Request req) {
        try {
            ClientRequest.sendRequestType(ClientRequestType.ADD_REQUEST);
            ClientRequest.output.writeObject(req);
            ServerResponseStatus status = (ServerResponseStatus) ClientRequest.input.readObject();

            return status.equals(ServerResponseStatus.OK);
        } catch (Exception e) {
            return false;
        }
    }

    //ToDo
    private boolean deleteTestDriveOnServer(TestDrive test) {
        try {
            ClientRequest.sendRequestType(ClientRequestType.DELETE_TEST_DRIVE);
            ClientRequest.output.writeObject(test.getId());
            ServerResponseStatus status = (ServerResponseStatus) ClientRequest.input.readObject();

            return status.equals(ServerResponseStatus.OK);
        } catch (Exception e) {
            return false;
        }
    }

    //ToDo
    private List<Client> getClientsFromServer() {
        try {
            ClientRequest.sendRequestType(ClientRequestType.GET_CLIENTS);
            List<Client> reqs = (List<Client>) ClientRequest.input.readObject();
            return reqs;
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void displayCars(List<Car> cars)
    {
        carsContainer.getChildren().clear();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(250);
        gridPane.setVgap(50);
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setStyle("-fx-padding: 20;");

        int column = 0;
        int row = 0;

        for (Car car : cars) {
            AnchorPane carPane = createCarPane(car);
            gridPane.add(carPane, column, row);

            column++;
            if (column >= 2) {
                column = 0;
                row++;
            }
        }

        carsContainer.getChildren().add(gridPane);
    }
    private AnchorPane createCarPane(Car car) {
        AnchorPane carPane = new AnchorPane();
        carPane.setPrefSize(400, 450);
        carPane.setStyle("-fx-background-color:  ORANGE; -fx-opacity: 0.6; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10;");

        AnchorPane.setTopAnchor(carPane, 50.0);
        AnchorPane.setLeftAnchor(carPane, (800 - carPane.getPrefWidth()) / 2);

        ImageView carImageView = new ImageView("file:///" + car.getImagePath());
        carImageView.setFitHeight(200);
        carImageView.setFitWidth(250);
        carImageView.setLayoutX((carPane.getPrefWidth() - carImageView.getFitWidth()) / 2);
        carImageView.setLayoutY(20);
        carPane.getChildren().add(carImageView);

        Label carBrandLabel = new Label("Марка: " + car.getBrand());
        carBrandLabel.setTextFill(Color.WHITE);
        carBrandLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 25px;");
        carBrandLabel.setLayoutX(10);
        carBrandLabel.setLayoutY(240);
        carPane.getChildren().add(carBrandLabel);

        Label carCostLabel = new Label("Цена: " + car.getCost() + " $");
        carCostLabel.setTextFill(Color.WHITE);
        carCostLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        carCostLabel.setLayoutX(10);
        carCostLabel.setLayoutY(280);
        carPane.getChildren().add(carCostLabel);

        Label petrolTypeLabel = new Label("Тип топлива: " + car.getPetrolType());
        petrolTypeLabel.setTextFill(Color.WHITE);
        petrolTypeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        petrolTypeLabel.setLayoutX(10);
        petrolTypeLabel.setLayoutY(320);
        carPane.getChildren().add(petrolTypeLabel);

        Label bodyTypeLabel = new Label("Тип кузова: " + car.getBodyType());
        bodyTypeLabel.setTextFill(Color.WHITE);
        bodyTypeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        bodyTypeLabel.setLayoutX(10);
        bodyTypeLabel.setLayoutY(350);
        carPane.getChildren().add(bodyTypeLabel);

        Button applyButton = new Button("Оформить заявку");
        applyButton.setLayoutX(10);
        applyButton.setLayoutY(400);
        carPane.getChildren().add(applyButton);

        applyButton.setOnAction(event -> handleApplyRequest(car));

        return carPane;
    }
    private void handleApplyRequest(Car car)
    {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Подтверждение заявки");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Вы уверены, что хотите оформить заявку на этот автомобиль?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            User currentUser = UserSession.getInstance().getUser();
            if (currentUser == null) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Вы должны быть авторизованы для оформления заявки!");
                errorAlert.showAndWait();
                return;
            }
            LocalDateTime currentDateTime = LocalDateTime.now();
            Request request = new Request(false,getClientByUser(currentUser),car,null,currentDateTime,null);
            boolean isMade = makeRequestOnServer(request);
            if(isMade)
            {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Успех");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Вы успешно оставили заявку.");
                successAlert.showAndWait();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Не удалось оставить заявку. Попробуйте ещё раз.");
                errorAlert.showAndWait();
            }
        }
    }
    public Client getClientByUser(User user) {
        List<Client> clients = getClientsFromServer();
        for (Client client : clients) {
            if (client.getUser().getId() == user.getId()) {
                return client;
            }
        }
        return null;
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
