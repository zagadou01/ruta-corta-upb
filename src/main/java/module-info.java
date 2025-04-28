module rutacortaupb.rutacortaupb {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens view to javafx.fxml;
    exports view;
    exports controller;
    opens controller to javafx.fxml;
}