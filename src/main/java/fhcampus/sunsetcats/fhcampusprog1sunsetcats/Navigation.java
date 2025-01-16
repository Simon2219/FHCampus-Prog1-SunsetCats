package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Navigation {
    // Load fxml to the main area (dynamicContentArea)
    static void loadContent(String fxmlFile) {
        UIController.getInstance().loadContent(fxmlFile);
    }

    // Load fxml into the area where an action is performed
    static void loadContentToCurrentVBOX(javafx.event.ActionEvent event, String fxmlFile) {
        try {
            Button sourceButton = (Button) event.getSource();
            VBox parentVBox = (VBox) sourceButton.getParent();
            FXMLLoader loader = new FXMLLoader(Navigation.class.getResource(fxmlFile));
            Node newContent = loader.load();
            parentVBox.getChildren().clear();
            parentVBox.getChildren().add(newContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load fxml into specified child VBox
    static void loadContentToArea(VBox vbox, String fxmlFile) {
        try {
            Pane content = FXMLLoader.load(Navigation.class.getResource(fxmlFile));
            vbox.getChildren().clear();
            vbox.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
