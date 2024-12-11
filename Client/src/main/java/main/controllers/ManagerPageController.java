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
import main.models.dto.Car;
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
    private ComboBox<String> checkCarBox;

    @FXML
    private VBox carsContainer;

    @FXML
    private Button checkRequestsButton;

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
        setupCarControlListener();

        logOut.setOnAction(event -> logOut(event));

    }

    private void setupCarControlListener() {
        checkCarBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleCarSelection(newValue);
        });
    }
    private void handleCheckRequestsPanel() {
        hideAllPanels();
        checkRequestsPanel.setVisible(true);
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
    private void initializeMakeRequestTable() {

        firstNameLastNameColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getClient().getUser();
            String fullName = user.getFirstName() + " " + user.getLastName();
            return new SimpleStringProperty(fullName);
        });

        usernameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient().getUser().getUsername()));
        carNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCar().getBrand()));
        phoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient().getPhoneNumber()));
        passportNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient().getPassportNumber()));

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

    private void handleCarSelection(String selectedValue) {
        List<Car> cars = getCarsFromServer();
        SorterContext sorterContext;
        if (selectedValue != null) {
            switch (selectedValue) {
                case "Просмотр авто":
                    hideAllPanels();
                    carControlPanel.setVisible(true);
                    displayCars(cars);
                    break;
                case "От самого дорогого к самому дешевому":
                    hideAllPanels();
                    carControlPanel.setVisible(true);
                    sorterContext = new SorterContext(new DescendingCostSortStrategy());
                    List<Car> sortedDescending = sorterContext.executeSort(new ArrayList<>(cars));
                    displayCars(sortedDescending);
                    break;
                case "От самого дешевого к самому дорогому":
                    hideAllPanels();
                    carControlPanel.setVisible(true);
                    sorterContext = new SorterContext(new AscendingCostSortStrategy());
                    List<Car> sortedAscending = sorterContext.executeSort(new ArrayList<>(cars));
                    displayCars(sortedAscending);
                    break;
                case "Сортировка по типу топлива":
                    hideAllPanels();
                    carControlPanel.setVisible(true);
                    sorterContext = new SorterContext(new PetrolTypeSortStrategy());
                    List<Car> sortedByPetrolType = sorterContext.executeSort(new ArrayList<>(cars));
                    displayCars(sortedByPetrolType);
                    break;
            }
        }
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
    private List<Car> getCarsFromServer() {
        try {
            ClientRequest.sendRequestType(ClientRequestType.GET_CARS);
            return (List<Car>) ClientRequest.input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
    private boolean sendRequestToServer(Request req) {
        req.setManager(UserSession.getInstance().getUser());
        try {
            ClientRequest.sendRequestType(ClientRequestType.ADD_REQUEST);
            ClientRequest.output.writeObject(req);
            ServerResponseStatus status = (ServerResponseStatus) ClientRequest.input.readObject();

            return status.equals(ServerResponseStatus.OK);
        } catch (Exception e) {
            return false;
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

        return carPane;
    }

    private void hideAllPanels()
    {
        carControlPanel.setVisible(false);
        startPanel.setVisible(false);
        workingWithRequestsPanel.setVisible(false);
        checkTestDrivePanel.setVisible(false);
        checkRequestsPanel.setVisible(false);
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