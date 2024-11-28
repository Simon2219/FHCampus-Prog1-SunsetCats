module fhcampus.sunsetcats.fhcampusprog1sunsetcats {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens fhcampus.sunsetcats.fhcampusprog1sunsetcats to javafx.fxml;
    exports fhcampus.sunsetcats.fhcampusprog1sunsetcats;
}