module examples.garagestate {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens GarageStateClass to javafx.fxml;
    exports GarageStateClass;

    opens GarageStateSwitch to javafx.fxml;
    exports GarageStateSwitch;
}