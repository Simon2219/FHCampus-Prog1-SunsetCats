package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Navigation {
    // Load the main screen from any window - button reference is used to find the current stage
    static void loadMain(Button button) {
        try {
            FXMLLoader fxml = new FXMLLoader(Navigation.class.getResource("main-view.fxml"));
            Parent root = fxml.load();
            Stage stage = (Stage) button.getScene().getWindow();
            Scene mainScene = new Scene(root);
            stage.setScene(mainScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Specify a specific scene to be loaded - button reference is used to find the current stage
    static void loadScene(Button button, String fxmlFile) {
        try {
            FXMLLoader fxml = new FXMLLoader(Navigation.class.getResource(fxmlFile));
            Parent root = fxml.load();
            Stage stage = (Stage) button.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load fxml into specified VBOX
    static void loadContentToArea(VBox pane, String fxmlFile) {
        try {
            Pane content = FXMLLoader.load(Navigation.class.getResource(fxmlFile));
            pane.getChildren().clear();
            pane.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void loadImmoDetail(VBox pane, String fxmlFile, Immobilie immo) {
        try {
            Pane content = FXMLLoader.load(Navigation.class.getResource("immo-view.fxml"));
            pane.getChildren().clear();
            pane.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load object details
    // TODO
}
