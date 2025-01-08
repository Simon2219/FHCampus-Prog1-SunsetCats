package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WelcomeController {
    @FXML
    public Label welcomeText;
    @FXML
    private Button newSearch;
    @FXML
    private ImageView logo;

    public void initialize() {
        welcomeText.setText("Wilkommen bei der Sunset Cats Immobiliensuche!");
        newSearch.setOnAction(_ -> Navigation.loadScene(newSearch,"search-view.fxml"));
        newSearch.setText("Starten Sie ihre erste Suche!");

        Image logoFile = new Image(getClass().getResource("SunsetCats_Logo_RotGelb.gif").toExternalForm());
        logo.setImage(logoFile);
    }
}
