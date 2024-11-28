package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    private ListView<String> menuListView;

    // Initialize the controller
    public void initialize() {
        // Add items to the ListView
        menuListView.getItems().addAll("Show Results", "Show Map", "Search");

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
        menuListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleMenuClick(newValue);
            }
        });
    }

    // Handles menu item selection
    private void handleMenuClick(String menuItem) {
        switch (menuItem) {
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
    private void displayResults() {
        welcomeText.setText("Displaying Results...");
    }

    private void displayMap() {
        welcomeText.setText("Displaying Map...");
    }

    private void displaySearch() {
        welcomeText.setText("Displaying Search...");
    }

    // Methods for button actions
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
}
