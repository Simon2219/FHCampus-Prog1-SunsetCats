package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.logging.Logger;

import static fhcampus.sunsetcats.fhcampusprog1sunsetcats.AppMain.willhabenConnector;

public class SearchController {
    @FXML
    private Button startSearch;

    @FXML
    private Button resetFilters;

    @FXML
    private VBox resultArea;

    @FXML
    private TextField searchField;

    @FXML
    private RadioButton radioWohnung;

    @FXML
    private RadioButton radioHaus;

    @FXML
    private RadioButton radioMiete;

    @FXML
    private RadioButton radioEigentum;

    @FXML
    private TextField priceFromField;

    @FXML
    private TextField priceToField;

    @FXML
    private TextField roomField;

    public void initialize() {
        Navigation.loadContentToArea(resultArea,"result-view.fxml");
    }

    @FXML
    private void startSearch() {
        // Auswahl Miet- oder Eigentumswohnung bildet die baseURL
        String baseURL = "https://www.willhaben.at/iad/immobilien/mietwohnungen/mietwohnung-angebote"; // Standardwert
        if (radioMiete.isSelected()) {
            baseURL = "https://www.willhaben.at/iad/immobilien/mietwohnungen/mietwohnung-angebote";
        } else if (radioEigentum.isSelected()) {
            baseURL = "https://www.willhaben.at/iad/immobilien/haus-kaufen/haus-angebote";
        }

        Search searchImmo = new Search(baseURL, false );

        // Benutzereingaben verarbeiten
        processFilters(searchImmo);

        // Suche starten
        ArrayList<Immobilie> results = willhabenConnector.startSearch(searchImmo);

        // Ergebnisse verarbeiten und anzeigen
        printResults(results);
        ResultStore.getInstance().setSearchResults(results);
        Navigation.loadContentToArea(resultArea,"result-view.fxml");
    }

    @FXML
    private void resetFilters() {
        // Alle Felder zurücksetzen
        searchField.clear();
        radioWohnung.setSelected(false);
        radioHaus.setSelected(false);
        radioMiete.setSelected(false);
        radioEigentum.setSelected(false);
        priceFromField.clear();
        priceToField.clear();
        roomField.clear();
    }

    /**
     * Verarbeitet die Benutzereingaben und fügt Filter in das Search-Objekt ein.
     *
     * @param search Das zu bearbeitende Search-Objekt.
     */
    private void processFilters(Search search) {
        // Keywords
        String keyword = searchField.getText();
        if (keyword != null && !keyword.isEmpty()) {
            search.addSearchFilter("keyword=" + keyword.replace(" ", "+"));
        }

        // Immobilientyp
        if (radioWohnung.isSelected()) {
            search.addSearchFilter("PROPERTY_TYPE_FLAT=true");
        } else if (radioHaus.isSelected()) {
            search.addSearchFilter("PROPERTY_TYPE_HOUSE=true");
        }

        /*
        // Art (Miete/Eigentum)
        if (radioMiete.isSelected()) {
            search.addSearchFilter("adTypeId=2"); // Beispielwert für Miete
        } else if (radioEigentum.isSelected()) {
            search.addSearchFilter("adTypeId=1"); // Beispielwert für Eigentum
        }
         */

        // Preisbereich
        String priceFrom = priceFromField.getText();
        if (priceFrom != null && !priceFrom.isEmpty()) {
            search.addSearchFilter("PRICE_FROM=" + priceFrom);
        }

        String priceTo = priceToField.getText();
        if (priceTo != null && !priceTo.isEmpty()) {
            search.addSearchFilter("PRICE_TO=" + priceTo);
        }

        /* Anzahl der Räume
        String rooms = roomField.getText();
        if (rooms != null && !rooms.isEmpty()) {
            try {
                int roomCount = Integer.parseInt(rooms.trim()); // Validierung der Eingabe als Zahl
                search.setMinRooms(roomCount); // Setze sowohl minRooms ...
                search.setMaxRooms(roomCount); // ... als auch maxRooms
            } catch (NumberFormatException e) {
                Debug.warning("Ungültige Eingabe für Räume: " + rooms);
                return; // Abbrechen, wenn die Eingabe ungültig ist
            }
        }

         */

        // Anzahl der Räume
        String rooms = roomField.getText();
        if (rooms != null && !rooms.isEmpty()) {
            search.addSearchFilter("NO_OF_ROOMS_BUCKET=" + rooms + "X" + rooms);
        }

        Debug.info("Applied Filters: " + search.getSearchFilters());
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
