package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.*;

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

    @FXML
    private VBox resultsEmpty;

    @FXML
    private VBox resultsExist;

    public void initialize() {
        if (!ResultStore.getInstance().getSearchResults().isEmpty()) {
            resultsEmpty.setVisible(false);
            resultsEmpty.setManaged(false);
            numOfResults.setText(ResultStore.getInstance().getSearchResults().size() + " Ergebnisse gefunden.");
            resultListView.getItems().addAll(ResultStore.getInstance().getSearchResults());
            resultListView.setCellFactory(param -> new ResultListCell());

        } else {
            resultsExist.setVisible(false);
            resultsExist.setManaged(false);
            statusText.setText("Noch keine Ergebnisse - bitte starten Sie zuerst eine Suche!");
            if (new File("previous-results.ser").isFile()) {
                loadPreviousResults.setVisible(true);
                loadPreviousResults.setText("Letzte Ergebnisse laden");
            }
        }

        resultListView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                // Pass selected object to the detail controller
                Immobilie selectedImmo = resultListView.getSelectionModel().getSelectedItem();
                ResultStore.getInstance().setSelectedImmo(selectedImmo);
                //

                Navigation.loadContent("details-view.fxml");
            }
        });
    }

    public void loadPrevResults(ActionEvent event) {
        ResultStore.getInstance().getPreviousResults();
        Navigation.loadContentToCurrentVBOX(event, "result-view.fxml");
    }
}