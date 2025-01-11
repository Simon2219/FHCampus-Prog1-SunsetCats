package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

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
    private VBox dynamicContentArea;

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
        Navigation.loadContentToArea(dynamicContentArea, "result-view.fxml");
    }

    @FXML
    void displaySearch() {
        Navigation.loadContentToArea(dynamicContentArea, "search-view.fxml");
        Stage stage = (Stage) dynamicContentArea.getScene().getWindow();
        stage.sizeToScene();
    }

    @FXML
    private void displayImprint() {
        Navigation.loadContentToArea(dynamicContentArea,"imprint-view.fxml");
    }

    @FXML
    private void displayWelcome() {
        Navigation.loadContentToArea(dynamicContentArea, "welcome-view.fxml");
    }

    @FXML
    private void exitApp() {
        javafx.application.Platform.exit();
    }

    // Helper method to load FXML files from menu
    void loadContent(String fxmlFile) {
        try {
            Pane content = FXMLLoader.load(getClass().getResource(fxmlFile));
            dynamicContentArea.getChildren().clear();
            dynamicContentArea.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Set Controller as singleton to call menu items from other Controllers
    private static UIController instance;

    public UIController() {
        instance = this;
    }

    public static UIController getInstance() {
        return instance;
    }
}