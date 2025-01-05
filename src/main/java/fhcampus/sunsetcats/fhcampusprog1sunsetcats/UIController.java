package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class UIController
{
    // Set Controller as singleton to call menu items from other Controllers
    private static UIController instance;
    public UIController() {
        instance = this;
    }
    public static UIController getInstance() {
        return instance;
    }

    @FXML
    private Label welcomeText;

    @FXML
    private VBox dynamicContentArea;

    @FXML
    private ListView<String> menuListView;

    // Initialize the controller
    public void initialize()
    {
        displayWelcome();

        // Add items to the ListView
        menuListView.getItems().addAll("Welcome", "Search", "Show Results", "Show Map");

        // Customize the cell factory to center-align the text
        menuListView.setCellFactory(new Callback<>()
        {
            @Override
            public ListCell<String> call(ListView<String> listView)
            {
                return new ListCell<>()
                {
                    @Override
                    protected void updateItem(String item, boolean empty)
                    {
                        super.updateItem(item, empty);

                        if (empty || item == null)
                        {
                            setText(null);
                        } else
                        {
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
            if (newValue != null)
            {
                handleMenuClick(newValue);
            }
        });
    }

    // Handles menu item selection
    private void handleMenuClick(String menuItem)
    {
        switch (menuItem)
        {
            case "Welcome":
                displayWelcome();
                break;
            case "Show Results":
                displayResults();
                break;
            case "Show Map":
                displayMap();
                break;
            case "Search":
                displaySearch();
                break;
            default:
                welcomeText.setText("Unknown menu action.");
                break;
        }
    }

    // Methods for menu actions
    public void displayResults()
    {
        loadContent("result-view.fxml");
    }

    private void displayMap()
    {
        loadContent("map-view.fxml");
    }

    private void displaySearch()
    {
        loadContent("search-view.fxml");
    }

    private void displayWelcome() {
        loadContent("welcome-view.fxml");
    }

    // Methods for button actions
    @FXML
    private void onOption1Click()
    {
        welcomeText.setText("Option 1 Selected: Performing Action...");
    }

    @FXML
    private void onOption2Click()
    {
        welcomeText.setText("Option 2 Selected: Performing Action...");
    }

    @FXML
    private void onOption3Click()
    {
        welcomeText.setText("Option 3 Selected: Performing Action...");
    }


    @FXML
    private void onMainActionClick()
    {
        welcomeText.setText("Option 3 Selected: Performing Action...");
    }


    // Helper method to load FXML files
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
