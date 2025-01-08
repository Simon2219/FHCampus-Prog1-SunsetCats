package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.IOException;

public class UIController {
    @FXML
    private Label welcomeText;

    @FXML
    private VBox dynamicContentArea;

    @FXML
    private ListView<String> menuListView;

    @FXML
    private Button imprint;

    // Initialize the controller
    public void initialize() {
        displayWelcome();

        // Add items to the ListView
        menuListView.getItems().addAll("Start", "Suche", "Letzte Ergebnisse", "Karte", "Suche 2");

        // Customize the cell factory to center-align the text
        menuListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item);
                            setStyle("-fx-alignment: center;"); // Center-align the text
                        }
                    }
                };
            }
        });

        // Add selection handling for clicks on list items
        menuListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue != null) {
                handleMenuClick(newValue);
            }
        });
    }

    // Handles menu item selection
    private void handleMenuClick(String menuItem) {
        switch (menuItem) {
            case "Start":
                displayWelcome();
                break;
            case "Letzte Ergebnisse":
                displayResults();
                break;
            case "Karte":
                displayMap();
                break;
            case "Suche":
                displaySearch();
                break;
            case "Suche 2":
                displaySearch2();
                break;
            default:
                System.out.println("Unknown menu action.");
                break;
        }
    }

    // Methods for menu actions
    private void displayResults() {
        Navigation.loadContentToArea(dynamicContentArea, "result-view.fxml");
    }

    private void displayMap() {
        Navigation.loadContentToArea(dynamicContentArea, "map-view.fxml");
    }

    private void displaySearch() {
        Navigation.loadScene(imprint,"search-view.fxml");
    }

    private void displaySearch2() {
        Navigation.loadContentToArea(dynamicContentArea, "search-view.fxml");
        Stage stage = (Stage) dynamicContentArea.getScene().getWindow();
        stage.sizeToScene();
    }

    private void displayWelcome() {
        Navigation.loadContentToArea(dynamicContentArea, "welcome-view.fxml");
    }



    // Methods for button actions
    @FXML
    private void displayImprint() {
        Navigation.loadContentToArea(dynamicContentArea,"imprint-view.fxml");
    }

    @FXML
    private void onOption1Click() {
        welcomeText.setText("Option 1 Selected: Performing Action...");
    }

    @FXML
    private void onOption2Click() {
        welcomeText.setText("Option 2 Selected: Performing Action...");
    }

    @FXML
    private void onOption3Click() {
        welcomeText.setText("Option 3 Selected: Performing Action...");
    }


    @FXML
    private void onMainActionClick() {
        welcomeText.setText("Option 3 Selected: Performing Action...");
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
