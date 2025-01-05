package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class ResultController {
    @FXML
    private ListView<Immobilie> resultListView;

    @FXML
    private Text errorText;

    @FXML
    private Text numOfResults;

    public void initialize() {
        if (!ResultStore.getInstance().getSearchResults().isEmpty()) {
            numOfResults.setText(ResultStore.getInstance().getSearchResults().size() + " results found.");
            resultListView.getItems().addAll(ResultStore.getInstance().getSearchResults());
            resultListView.setCellFactory(param -> new ResultListCell());
        } else {
            errorText.setText("No results available yet, please start a search first!");
        }
    }
}