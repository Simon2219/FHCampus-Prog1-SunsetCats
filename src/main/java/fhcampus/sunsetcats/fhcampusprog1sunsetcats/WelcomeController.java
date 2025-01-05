package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeController {
    @FXML
    private Button newSearch;

    public void initialize() {
        newSearch.setOnAction(_ -> UIController.getInstance().loadContent("search-view.fxml"));
    }
}
