module main {
    requires javafx.controls;
    requires javafx.fxml;

    opens main.controllers to javafx.fxml; // Открываем пакет controllers для FXML
    opens main.entities to javafx.fxml; // Открываем пакет entities для FXML, если нужно
    opens main.enums to javafx.fxml; // Открываем пакет enums для FXML, если нужно

    exports main;
}