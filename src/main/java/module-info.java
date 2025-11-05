module org.example.baitaplon {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.example.baitaplon to javafx.fxml;
    exports org.example.baitaplon;
}