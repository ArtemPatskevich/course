module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires static lombok;

    opens main.controllers to javafx.fxml; // Открываем пакет controllers для FXML

    exports main;
    opens main.enums.entityAttributes to javafx.fxml;
}