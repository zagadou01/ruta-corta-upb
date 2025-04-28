module core {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports view;
    exports controller;
    exports model;

    opens view to javafx.fxml;
    opens controller to javafx.fxml;
}