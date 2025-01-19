package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.scene.image.ImageView;


public class UIController {
    @FXML
    private Label welcomeText;

    @FXML
    VBox dynamicContentArea;

    @FXML
    private Button imprint;

    @FXML
    private Button search;

    @FXML
    private Button results;

    @FXML
    private Button exit;

    @FXML
    private Button welcome;

    @FXML
    private ImageView smallLogo;

    // Initialize the controller
    public void initialize() {
//        Image logoFile = new Image(getClass().getResource("SunsetCats_Logo_Web_RotGelb.png").toExternalForm());
//        smallLogo.setImage(logoFile);
        displayWelcome();
    }

    // Methods for menu actions
    @FXML
    private void displayResults() {
        Navigation.loadContent("result-view.fxml");
    }

    @FXML
    void displaySearch() {
        Navigation.loadContent("search-view.fxml");
        Stage stage = (Stage) dynamicContentArea.getScene().getWindow();
        stage.setWidth(1200);  // Standardbreite
        stage.setHeight(800); // Standardhöhe

    }

    @FXML
    private void displayImprint() {
        Navigation.loadContent("imprint-view.fxml");
    }

    @FXML
    private void displayWelcome() {
        Navigation.loadContent("welcome-view.fxml");
    }

    @FXML
    private void exitApp() {
        javafx.application.Platform.exit();
    }

    // HELPER METHODS FOR NAVIGATION
    // Set Controller as singleton to from other Controllers
    private static UIController instance;
    public UIController() {
        instance = this;
    }
    public static UIController getInstance() {
        return instance;
    }

    // Helper method to load FXML files to the main content area
    void loadContent(String fxmlFile) {
        try {
            Pane content = FXMLLoader.load(getClass().getResource(fxmlFile));
            dynamicContentArea.getChildren().clear();
            dynamicContentArea.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}