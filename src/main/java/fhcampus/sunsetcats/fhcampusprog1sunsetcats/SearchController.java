package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static fhcampus.sunsetcats.fhcampusprog1sunsetcats.AppMain.willhabenConnector;

public class SearchController {

    // Standard-Felder für die Filtersuche
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

    // Neue Felder für Bundesländer und Bezirke
    @FXML
    private VBox districtDropdownContainer; // Container für Bezirke/Behörden
    @FXML
    private CheckComboBox<String> locationCheckComboBox; // Dropdown für Bundesländer
    @FXML
    private CheckComboBox<String> districtCheckComboBox; // Dropdown für Bezirke/Behörden

    private final Logger debugLogger = Logger.getLogger(SearchController.class.getName());

    public void initialize() {
        // Bestehende Initialisierungen
        debugLogger.info("SearchController initialized");
        Navigation.loadContentToArea(resultArea, "result-view.fxml");

        // Initialisieren der zusätzlichen Dropdown-Funktionen für Bezirke/Bundesländer
        setupLocationCheckComboBox();
        setupDropdownVisibilityLogic();
        updateDistrictDropdownVisibility();
    }

    @FXML
    private void startSearch() {
        // Standard-BaseURL
        String baseURL = "https://www.willhaben.at/iad/immobilien";
        if (radioMiete.isSelected()) {
            baseURL = "https://www.willhaben.at/iad/immobilien/mietwohnungen/mietwohnung-angebote";
        } else if (radioEigentum.isSelected()) {
            baseURL = "https://www.willhaben.at/iad/immobilien/haus-kaufen/haus-angebote";
        }

        Search searchImmo = new Search(baseURL, false);

        // Filter anwenden
        processFilters(searchImmo);

        // Suche ausführen
        ArrayList<Immobilie> results = willhabenConnector.startSearch(searchImmo);

        // Ergebnisse anzeigen
        printResults(results);
        ResultStore.getInstance().setSearchResults(results);
        Navigation.loadContentToArea(resultArea, "result-view.fxml");
    }

    @FXML
    private void resetFilters() {
        // Standardfelder zurücksetzen
        searchField.clear();
        radioWohnung.setSelected(false);
        radioHaus.setSelected(false);
        radioMiete.setSelected(false);
        radioEigentum.setSelected(false);
        priceFromField.clear();
        priceToField.clear();
        roomField.clear();

        // Dropdown-Filter zurücksetzen
        locationCheckComboBox.getCheckModel().clearChecks();
        districtCheckComboBox.getCheckModel().clearChecks();
    }

    /**
     * Verarbeitet die Benutzereingaben und fügt Filter in das `Search`-Objekt ein.
     *
     * @param search Das zu bearbeitende `Search`-Objekt.
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

        /* Art (Miete/Eigentum)
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

        // Ausgewählte Bezirke
        List<String> selectedDistricts = districtCheckComboBox.getCheckModel().getCheckedItems();
        if (!selectedDistricts.isEmpty()) {
            search.addSearchFilter("districts=" + String.join(",", selectedDistricts));
        }

        // Ausgewählte Bundesländer ("areaId")
        String areaIdFilter = generateAreaIdString();
        if (!areaIdFilter.isEmpty()) {
            search.addSearchFilter(areaIdFilter);
        }

        debugLogger.info("Applied Filters: " + search.getSearchFilters());
    }

    private static void printResults(ArrayList<Immobilie> searchResults) {
        for (Immobilie currentImmobilie : searchResults) {
            Double immoPrice = currentImmobilie.getAttribute(Immobilie.AttributeKey.PRICE);
            Logger.getLogger(SearchController.class.getName()).info(
                    String.format("ID: %s | PRICE: %f - %s",
                            currentImmobilie.getAttribute(Immobilie.AttributeKey.ID),
                            immoPrice,
                            currentImmobilie.getAttribute(Immobilie.AttributeKey.DESCRIPTION))
            );
        }
    }

    // --- Dropdown-Logik für Bezirke/Bundesländer ---

    private void setupLocationCheckComboBox() {
        locationCheckComboBox.getItems().addAll(
                "Wien", "Niederösterreich", "Burgenland", "Oberösterreich",
                "Steiermark", "Kärnten", "Salzburg", "Tirol", "Vorarlberg"
        );
    }

    private void setupDropdownVisibilityLogic() {
        locationCheckComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            updateDistrictDropdownVisibility();
        });
    }

    private void updateDistrictDropdownVisibility() {
        List<String> selectedStates = locationCheckComboBox.getCheckModel().getCheckedItems();

        if (selectedStates.isEmpty()) {
            districtDropdownContainer.setVisible(false);
            districtDropdownContainer.setManaged(false);
            return;
        }

        updateDistrictCheckComboBox(selectedStates);
        districtDropdownContainer.setVisible(true);
        districtDropdownContainer.setManaged(true);
    }

    private void updateDistrictCheckComboBox(List<String> selectedStates) {
        List<String> combinedDistricts = new ArrayList<>();

        if (selectedStates.contains("Wien")) {
            combinedDistricts.addAll(getDistrictsForWien());
        }
        if (selectedStates.contains("Niederösterreich")) {
            combinedDistricts.addAll(getDistrictsForNiederoesterreich());
        }
        if (selectedStates.contains("Burgenland")) {
            combinedDistricts.addAll(getDistrictsForBurgenland());
        }
        if (selectedStates.contains("Oberösterreich")) {
            combinedDistricts.addAll(getDistrictsForOberoesterreich());
        }
        if (selectedStates.contains("Steiermark")) {
            combinedDistricts.addAll(getDistrictsForSteiermark());
        }
        if (selectedStates.contains("Kärnten")) {
            combinedDistricts.addAll(getDistrictsForKaernten());
        }
        if (selectedStates.contains("Salzburg")) {
            combinedDistricts.addAll(getDistrictsForSalzburg());
        }
        if (selectedStates.contains("Tirol")) {
            combinedDistricts.addAll(getDistrictsForTirol());
        }
        if (selectedStates.contains("Vorarlberg")) {
            combinedDistricts.addAll(getDistrictsForVorarlberg());
        }

        districtCheckComboBox.getItems().setAll(combinedDistricts);
    }

    /**
     * Generiert den "areaId=<Nummer>"-String für die ausgewählten Bundesländer
     * und verknüpft diese mit "&".
     *
     * @return Ein String mit den verknüpften "areaId=<Nummer>"-Werten.
     */
    private String generateAreaIdString() {
        List<String> selectedStates = locationCheckComboBox.getCheckModel().getCheckedItems();

        if (selectedStates.isEmpty()) {
            return "areaId=9"; // Standardwert für Wien
        }

        return selectedStates.stream()
                .map(this::getAreaIdStringForState) // Konvertiere jedes ausgewählte Bundesland
                .filter(id -> !id.isEmpty()) // Entferne ungültige Werte
                .collect(Collectors.joining("&")); // Verknüpfe mit "&"
    }

    /**
     * Gibt den entsprechenden "areaId=<Nummer>"-String für ein Bundesland zurück.
     *
     * @param state Der Name des Bundeslands.
     * @return Der "areaId=<Nummer>"-String oder ein leerer String bei ungültiger Eingabe.
     */
    private String getAreaIdStringForState(String state) {
        switch (state) {
            case "Burgenland":
                return "areaId=1";
            case "Kärnten":
                return "areaId=2";
            case "Niederösterreich":
                return "areaId=3";
            case "Oberösterreich":
                return "areaId=4";
            case "Salzburg":
                return "areaId=5";
            case "Steiermark":
                return "areaId=6";
            case "Tirol":
                return "areaId=7";
            case "Vorarlberg":
                return "areaId=8";
            case "Wien":
                return "areaId=9";
            default:
                return ""; // Kein gültiges Bundesland
        }
    }

    // Methoden für Bezirke

    private List<String> getDistrictsForWien() {
        return List.of(
                "1. Innere Stadt", "2. Leopoldstadt", "3. Landstraße", "4. Wieden", "5. Margareten",
                "6. Mariahilf", "7. Neubau", "8. Josefstadt", "9. Alsergrund", "10. Favoriten",
                "11. Simmering", "12. Meidling", "13. Hietzing", "14. Penzing", "15. Rudolfsheim-Fünfhaus",
                "16. Ottakring", "17. Hernals", "18. Währing", "19. Döbling", "20. Brigittenau",
                "21. Floridsdorf", "22. Donaustadt", "23. Liesing"
        );
    }

    private List<String> getDistrictsForNiederoesterreich() {
        return List.of(
                "Amstetten", "Baden", "Bruck an der Leitha", "Gänserndorf", "Gmünd",
                "Hollabrunn", "Horn", "Korneuburg", "Krems an der Donau (Stadt)",
                "Krems-Land", "Lilienfeld", "Melk", "Mistelbach", "Mödling",
                "Neunkirchen", "St. Pölten (Stadt)", "St. Pölten-Land", "Scheibbs",
                "Tulln", "Waidhofen an der Thaya", "Waidhofen an der Ybbs (Stadt)",
                "Wiener Neustadt (Stadt)", "Wiener Neustadt-Land", "Zwettl"
        );
    }

    private List<String> getDistrictsForBurgenland() {
        return List.of(
                "Eisenstadt (Stadt)", "Eisenstadt-Umgebung", "Güssing", "Jennersdorf",
                "Mattersburg", "Neusiedl am See", "Oberpullendorf", "Oberwart", "Rust (Stadt)"
        );
    }

    private List<String> getDistrictsForOberoesterreich() {
        return List.of(
                "Linz (Stadt)", "Linz-Land", "Braunau am Inn", "Eferding", "Freistadt",
                "Gmunden", "Grieskirchen", "Kirchdorf an der Krems", "Perg",
                "Ried im Innkreis", "Rohrbach", "Schärding", "Steyr (Stadt)",
                "Steyr-Land", "Urfahr-Umgebung", "Vöcklabruck", "Wels (Stadt)", "Wels-Land"
        );
    }

    private List<String> getDistrictsForSteiermark() {
        return List.of(
                "Graz (Stadt)", "Graz-Umgebung", "Deutschlandsberg",
                "Hartberg-Fürstenfeld", "Leibnitz", "Leoben", "Liezen",
                "Murau", "Murtal", "Südoststeiermark", "Voitsberg", "Weiz"
        );
    }

    private List<String> getDistrictsForKaernten() {
        return List.of(
                "Klagenfurt am Wörthersee (Stadt)", "Klagenfurt-Land",
                "Villach (Stadt)", "Villach-Land", "Feldkirchen",
                "Hermagor", "Spittal an der Drau", "St. Veit an der Glan",
                "Völkermarkt", "Wolfsberg"
        );
    }

    private List<String> getDistrictsForSalzburg() {
        return List.of(
                "Salzburg (Stadt)", "Salzburg-Umgebung", "Hallein",
                "St. Johann im Pongau", "Tamsweg", "Zell am See"
        );
    }

    private List<String> getDistrictsForTirol() {
        return List.of(
                "Innsbruck (Stadt)", "Innsbruck-Land", "Imst", "Kitzbühel",
                "Kufstein", "Landeck", "Lienz", "Reutte", "Schwaz"
        );
    }

    private List<String> getDistrictsForVorarlberg() {
        return List.of(
                "Bregenz", "Dornbirn", "Feldkirch", "Bludenz"
        );
    }
}