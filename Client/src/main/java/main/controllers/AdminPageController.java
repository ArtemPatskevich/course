package main.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import main.Strategy.AscendingCostSortStrategy;
import main.Strategy.DescendingCostSortStrategy;
import main.Strategy.PetrolTypeSortStrategy;
import main.Strategy.SorterContext;
import main.enums.entityAttributes.BodyType;
import main.enums.entityAttributes.PetrolType;
import main.enums.entityAttributes.RoleName;
import main.enums.requests.ClientRequestType;
import main.enums.status.ServerResponseStatus;
import main.models.dto.*;
import main.utils.UserSession;
import main.utils.tcp.ClientRequest;

import java.io.BufferedWriter;
import java.io.File;
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
    private ComboBox<String> checkCarBox;

    @FXML
    private AnchorPane checkUserPanel;
    @FXML
    private AnchorPane deleteUserPanel;
    @FXML
    private Button makingReport;
    @FXML
    private VBox carsContainer;
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
    private TableColumn<Car, BodyType> carTypeColumnUpd;
    @FXML
    private TableColumn<Car, PetrolType> fuelTypeColumnUpd;
    @FXML
    private TableColumn<Car, Void> CarsColumnUpd;

    @FXML
    private AnchorPane addCarPanel;
    @FXML
    private ComboBox<BodyType> carTypeChoose;
    @FXML
    private ComboBox<PetrolType> petrolTypeChoose;
    @FXML
    private TextField brandField;
    @FXML
    private TextField carCostField;
    @FXML
    private Button chooseCarFile;
    @FXML
    private Button addCarButton;

    String selectedFilePath = "";

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
        setupCarCheckListener();
        addCarButton.setOnAction(event -> handleAddCar());

        chooseCarFile.setOnAction(event -> openFileChooser());

        for (BodyType type : BodyType.values()) {
            carTypeChoose.getItems().add(type);
        }
        for (PetrolType type : PetrolType.values()) {
            petrolTypeChoose.getItems().add(type);
        }
    }
    private void setupCarCheckListener() {
        checkCarBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleCarSelection(newValue);
        });
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
                    if(isDeleted) {
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
        priceColumnUpd.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getCost()).asObject());
        carTypeColumnUpd.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBodyType()));
        fuelTypeColumnUpd.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPetrolType()));

        brandColumnUpd.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumnUpd.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        carTypeColumnUpd.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(BodyType.values())));
        carTypeColumnUpd.setOnEditCommit(event -> {
            Car car = event.getRowValue();
            if (event.getNewValue() != null) {
                car.setBodyType(event.getNewValue());
            }
        });

        fuelTypeColumnUpd.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(PetrolType.values())));
        fuelTypeColumnUpd.setOnEditCommit(event -> {
            Car car = event.getRowValue();
            if (event.getNewValue() != null) {
                car.setPetrolType(event.getNewValue());
            }
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

    private boolean deleteCarOnServer(Car car) {
        try {
            ClientRequest.sendRequestType(ClientRequestType.DELETE_CAR);
            ClientRequest.output.writeObject(car);
            ServerResponseStatus status = (ServerResponseStatus) ClientRequest.input.readObject();
            return status.equals(ServerResponseStatus.OK);
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    private void deleteUserOnServer(User user) {
        try {
            ClientRequest.sendRequestType(ClientRequestType.DELETE_USER);
            ClientRequest.output.writeObject(user.getId());
            ServerResponseStatus status = (ServerResponseStatus) ClientRequest.input.readObject();

            if(status.equals(ServerResponseStatus.OK)) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Успех");
                successAlert.setHeaderText("Пользователь успешно удален");
                successAlert.showAndWait();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Ошибка");
                errorAlert.setHeaderText("Не удалось удалить пользователя!");
                errorAlert.showAndWait();
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
                case "Удалить автомобили":
                    closePanels();
                    deleteCarPanel.setVisible(true);
                    addCarsToDeleteTable(getCarsFromServer());
                    break;
                case "Добавить автомобиль":
                    closePanels();
                    addCarPanel.setVisible(true);
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

    private void handleAddCar() {
        String brand = brandField.getText().trim();
        String costStr = carCostField.getText().trim();
        BodyType selectedCarType = carTypeChoose.getValue();
        PetrolType selectedPetrolType = petrolTypeChoose.getValue();

        boolean isCorrect = true;

        if (brand.isEmpty() || costStr.isEmpty() || selectedCarType == null || selectedPetrolType == null) {
            showAlert("Ошибка", "Пожалуйста, заполните все поля.");
            isCorrect = false;
            return;
        }
        double cost= 0;
        try {
             cost = Double.parseDouble(costStr);
            System.out.println("Car added: " + brand + ", Cost: " + cost + ", Type: " + selectedCarType + ", Petrol: " + selectedPetrolType);
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Стоимость должна быть числом.");
            isCorrect = false;
        }
        if (isCorrect)
        {
            Car car = new Car(brand,cost,selectedPetrolType,selectedCarType,selectedFilePath);
            boolean added = addCarToSystem(car);

            if(added)
            {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Успешное добавление");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Автомобиль успешно добавлен.");
                successAlert.showAndWait();
                clearFields();
            }
            else {
                showAlert("Ошибка", "Некорректное добавление");
                clearFields();
            }
        }
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

    private boolean addCarToSystem(Car car) {
        try {
            ClientRequest.sendRequestType(ClientRequestType.ADD_CAR);
            ClientRequest.output.writeObject(car);
            ServerResponseStatus status = (ServerResponseStatus) ClientRequest.input.readObject();

            return status.equals(ServerResponseStatus.OK);
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    private void clearFields() {
        brandField.clear();
        carCostField.clear();
        carTypeChoose.getSelectionModel().clearSelection();
        petrolTypeChoose.getSelectionModel().clearSelection();
        selectedFilePath = "";
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateCar(Car car) {
        try {
            ClientRequest.sendRequestType(ClientRequestType.UPDATE_CAR);
            ClientRequest.output.writeObject(car);
            ServerResponseStatus status = (ServerResponseStatus) ClientRequest.input.readObject();

            if(status.equals(ServerResponseStatus.OK)) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Успех");
                successAlert.setHeaderText("Автомобиль успешно обновлен");
                successAlert.showAndWait();
            } else {
                Alert successAlert = new Alert(Alert.AlertType.ERROR);
                successAlert.setTitle("Ошибка");
                successAlert.setHeaderText("Не удалось обновить автомобиль!");
                successAlert.showAndWait();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException();
        }
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

        return carPane;
    }

    public void closePanels() {
        startPanel.setVisible(false);
        carControlPanel.setVisible(false);
        checkUserPanel.setVisible(false);
        deleteUserPanel.setVisible(false);
        deleteCarPanel.setVisible(false);
        addCarPanel.setVisible(false);
    }
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Изображения (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);
        File file = fileChooser.showOpenDialog(chooseCarFile.getScene().getWindow());

        if (file != null) {
            selectedFilePath = file.getAbsolutePath();
            System.out.println(selectedFilePath);
        } else {
            System.out.println("file not choosen");
        }
    }
}