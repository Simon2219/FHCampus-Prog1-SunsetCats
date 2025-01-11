package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.logging.Logger;

import static fhcampus.sunsetcats.fhcampusprog1sunsetcats.AppMain.willhabenConnector;

public class SearchController {
    @FXML
    private Button startSearch;

    @FXML
    private Button goBack;

    @FXML
    private VBox resultArea;

    public void initialize() {
        Navigation.loadContentToArea(resultArea,"result-view.fxml");
    }

    @FXML
    private void startSearch() {
        Search searchImmo = new Search("https://www.willhaben.at/iad/immobilien/haus-kaufen/haus-angebote?sfId=4d397651-95f3-402c-8fd4-8eb4c15d49a8&isNavigation=true&rows=30&areaId=900&NO_OF_ROOMS_BUCKET=5X5",
                false );
        ArrayList<Immobilie> results = willhabenConnector.startSearch(searchImmo);
        printResults(results);
        ResultStore.getInstance().setSearchResults(results);
        Navigation.loadContentToArea(resultArea,"result-view.fxml");
    }

    private static final Logger Debug = Logger.getLogger(AppMain.class.getName());
    private static void printResults(ArrayList<Immobilie> searchResults)
    {
        for (Immobilie currentImmobilie : searchResults)
        {
            Double immoPrice = currentImmobilie.getAttribute(Immobilie.AttributeKey.PRICE);
            Debug.info(String.format("ID: %s | PRICE: %f  -  %s", currentImmobilie.getAttribute(Immobilie.AttributeKey.ID),
                    immoPrice, currentImmobilie.getAttribute(Immobilie.AttributeKey.DESCRIPTION)));
        }
        System.out.println(searchResults.size());
    }
}
