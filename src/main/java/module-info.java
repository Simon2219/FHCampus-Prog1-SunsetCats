module fhcampus.sunsetcats.fhcampusprog1sunsetcats {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires org.json;
    requires java.net.http;
    requires org.jsoup;
    requires java.sql;

    opens fhcampus.sunsetcats.fhcampusprog1sunsetcats to javafx.fxml;
    exports fhcampus.sunsetcats.fhcampusprog1sunsetcats;
    exports fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling;
    opens fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling to javafx.fxml;
}