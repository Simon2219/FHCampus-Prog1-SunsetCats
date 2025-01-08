package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.*;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;

import java.io.File;

public class ResultController {
    @FXML
    private ListView<Immobilie> resultListView;

    @FXML
    private Text statusText;

    @FXML
    private Text numOfResults;

    @FXML
    private Button loadPreviousResults;

    public void initialize() {
        if (!ResultStore.getInstance().getSearchResults().isEmpty()) {
            numOfResults.setText(ResultStore.getInstance().getSearchResults().size() + " Ergebnisse gefunden.");
            resultListView.getItems().addAll(ResultStore.getInstance().getSearchResults());
            resultListView.setCellFactory(param -> new ResultListCell());

        } else {
            resultListView.setVisible(false);
            resultListView.setManaged(false);
            statusText.setText("Noch keine Ergebnisse - bitte starten Sie zuerst eine Suche!");
            if (new File("previous-results.ser").isFile()) {
                loadPreviousResults.setVisible(true);
                loadPreviousResults.setText("Letzte Ergebnisse laden");
            }
        }

        loadPreviousResults.setOnAction(_ -> {
            ResultStore.getInstance().getPreviousResults();
        });

        resultListView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                Immobilie currentItem = resultListView.getSelectionModel().getSelectedItem();
                UIController.getInstance().loadContent("immo-view.fxml");
            }
        });
    }
}