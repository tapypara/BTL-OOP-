module org.example.baitaplon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;

    opens org.example.baitaplon to javafx.fxml;
    exports org.example.baitaplon;
}