package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static fhcampus.sunsetcats.fhcampusprog1sunsetcats.AppMain.willhabenConnector;

public class SearchController {
    String baseURL = "https://www.willhaben.at/iad/immobilien";
    String finalURL = "";

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
    @FXML
    private TextField areaFromField;
    @FXML
    private TextField areaToField;

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
        // Wohnung und Haus mit Miete/Eigentum prüfen und URL entsprechend setzen
        if (radioWohnung.isSelected() && radioMiete.isSelected()) {
            baseURL += "/mietwohnungen/mietwohnung-angebote?";
        } else if (radioWohnung.isSelected() && radioEigentum.isSelected()) {
            baseURL += "/eigentumswohnung/eigentumswohnung-angebote?";
        } else if (radioHaus.isSelected() && radioMiete.isSelected()) {
            baseURL += "/haus-mieten/haus-angebote?";
        } else if (radioHaus.isSelected() && radioEigentum.isSelected()) {
            baseURL += "/haus-kaufen/haus-angebote?";
        }

        // Speichert die Auswahl der Art (Miete/Eigentum) im Resultstore,
        // damit sie vom ImmoDetailController abgefragt werden kann
        String immoType = getSelectedImmoType();
        ResultStore.getInstance().setSelectedType(immoType);

        // Erstelle die Filter und formatiere sie als URL-Teile
        String areaIdFilter = generateAreaOrDistrictIdString();
        String RoomsFilter = getRooms();
        String FromPriceFilter = getPriceFrom();
        String ToPriceFilter = getPriceTo();
        String areaFromFilter = getSizeFrom();
        String areaToFilter = getSizeTo();
        String keywordsFilter = getKeywords();

        // Test: Print der areaIdFilter zur Überprüfung
        System.out.println("Generierter areaId-Filter: " + areaIdFilter); // Test-Ausgabe

        finalURL = baseURL + areaIdFilter + RoomsFilter + FromPriceFilter + ToPriceFilter + areaFromFilter + areaToFilter + keywordsFilter;
        //String finalURL = baseURL + areaIdFilter;
        System.out.println("Finale Base-URL: " + finalURL);

        Search searchImmo = new Search(finalURL, false);

        // Suche ausführen
        ArrayList<Immobilie> results = willhabenConnector.startSearch(searchImmo);

        // Ergebnisse anzeigen
        printResults(results);
        ResultStore.getInstance().setSearchResults(results);
        Navigation.loadContentToArea(resultArea, "result-view.fxml");
    }

    @FXML
    private void resetFilters(ActionEvent event) {
        // Standardfelder zurücksetzen
        searchField.clear();
        radioWohnung.setSelected(false);
        radioHaus.setSelected(false);
        radioMiete.setSelected(false);
        radioEigentum.setSelected(false);
        priceFromField.clear();
        priceToField.clear();
        roomField.clear();
        areaFromField.clear();
        areaToField.clear();

        // Dropdown-Filter zurücksetzen
        locationCheckComboBox.getCheckModel().clearChecks();
        districtCheckComboBox.getCheckModel().clearChecks();
    }

    public String getSelectedImmoType() {
        if (radioMiete.isSelected()) {
            return "Miete";
        } else if (radioEigentum.isSelected()) {
            return "Eigentum";
        }
        return "Unbekannt";
    }

    public String getRooms() {
        String rooms = roomField.getText();
        String amountRooms = "";
        if (rooms != null && !rooms.isEmpty()) {
            amountRooms = ("&NO_OF_ROOMS_BUCKET=" + rooms + "X" + rooms);
        }
        return finalURL + amountRooms;
    }

    public String getPriceFrom() {
        String priceFrom = priceFromField.getText();
        String amountPriceFrom = "";
        if (priceFrom != null && !priceFrom.isEmpty()) {
            amountPriceFrom = ("&PRICE_FROM=" + priceFrom);
        }
        return finalURL + amountPriceFrom;
    }

    public String getPriceTo() {
        String priceTo = priceToField.getText();
        String amountPriceTo = "";
        if (priceTo != null && !priceTo.isEmpty()) {
            amountPriceTo = ("&PRICE_TO=" + priceTo);
        }
        return finalURL + amountPriceTo;
    }

    public String getSizeFrom() {
        String sizeFrom = areaFromField.getText();
        String amountSizeFrom = "";
        if (sizeFrom != null && !sizeFrom.isEmpty()) {
            amountSizeFrom = ("&ESTATE_SIZE/LIVING_AREA_FROM=" + sizeFrom);
        }
        return finalURL + amountSizeFrom;
    }

    public String getSizeTo() {
        String sizeTo = areaToField.getText();
        String amountSizeTo = "";
        if (sizeTo != null && !sizeTo.isEmpty()) {
            amountSizeTo = ("&ESTATE_SIZE/LIVING_AREA_TO=" + sizeTo);
        }
        return finalURL + amountSizeTo;
    }

    // Keywords als URL-Teil formatieren
    public String getKeywords() {
        String keywordsText = searchField.getText();
        String allKeywords = "";

        if (!keywordsText.isEmpty()){
            // Keyword-Liste erstellen
            // Satzzeichen entfernen, Sonderzeichen kodieren und leere Keywords filtern
            List<String> keywords = Arrays.stream(keywordsText.split("\\s+"))
                    .map(String::trim)
                    .map(keyword -> keyword.replaceAll("[^a-zA-Z0-9äöüÄÖÜß]", ""))
                    .map(keyword -> URLEncoder.encode(keyword, StandardCharsets.UTF_8))
                    .filter(keyword -> !keyword.isEmpty())
                    .collect(Collectors.toList());

            // Keywords zur URL hinzufügen
            for (String keyword : keywords) {
                allKeywords += "&keyword=" + keyword;
            }
        }
        return finalURL + allKeywords;
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
        // Listener für die Auswahl in der locationCheckComboBox (Bundesländer)
        locationCheckComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            // Leere die Bezirke, wenn das Bundesland geändert wird
            districtCheckComboBox.getCheckModel().clearChecks();

            // Aktualisiere die Sichtbarkeit der Bezirksauswahl basierend auf dem ausgewählten Bundesland
            updateDistrictDropdownVisibility();

            // Test-Ausgabe (debugging)
            System.out.println("Bundesland geändert - Bezirke wurden geleert.");
        });

        // Listener für die Auswahl in der districtCheckComboBox (Bezirke)
        districtCheckComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            // Optional: Aktionen bei Änderung der Bezirksauswahl (falls benötigt)
            System.out.println("Bezirksauswahl geändert.");
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

    /**
     * Generiert den passenden areaId-String, abhängig von der Auswahl der Bezirke oder der Bundesländer.
     * Wenn Bezirke ausgewählt wurden, wird der String für die Bezirke erzeugt.
     * Ansonsten wird die areaId des Bundeslands verwendet.
     */
    private String generateAreaOrDistrictIdString() {
        // Mapping abh. vom Bundesland
        Map<String, String> districtIds = new HashMap<>();
        districtIds.putAll(getDistrictIdsForNiederoesterreich());
        districtIds.putAll(getDistrictIdsForVorarlberg());
        districtIds.putAll(getDistrictIdsForBurgenland());
        districtIds.putAll(getDistrictIdsForKaernten());
        districtIds.putAll(getDistrictIdsForOberoesterreich());
        districtIds.putAll(getDistrictIdsForSalzburg());
        districtIds.putAll(getDistrictIdsForSteiermark());
        districtIds.putAll(getDistrictIdsForTirol());
        districtIds.putAll(getDistrictIdsForWien());

        List<String> selectedDistricts = districtCheckComboBox.getCheckModel().getCheckedItems();

        if (!selectedDistricts.isEmpty()) {
            return selectedDistricts.stream()
                    .map(districtIds::get)
                    .filter(Objects::nonNull)
                    .map(id -> "areaId=" + id)
                    .collect(Collectors.joining("&"));
        }

        return generateAreaIdString();
    }

    /**
     * Gibt die Map mit den IDs aller Bezirke in Niederösterreich zurück.
     * Die IDs hier sind dreistellig, wobei die erste Zahl (z. B. "3") verwendet wird.
     */

    private Map<String, String> getDistrictIdsForBurgenland() {
        Map<String, String> districtIds = new HashMap<>();
        districtIds.put("Eisenstadt", "101");
        districtIds.put("Eisenstadt - Umgebung", "103");
        districtIds.put("Güssing", "104");
        districtIds.put("Jennersdorf", "105");
        districtIds.put("Mattersburg", "106");
        districtIds.put("Neusiedl am See", "107");
        districtIds.put("Oberpullendorf", "108");
        districtIds.put("Oberwart", "109");
        districtIds.put("Rust (Stadt)", "102");
        return districtIds;
    }

    private Map<String, String> getDistrictIdsForKaernten() {
        Map<String, String> districtIds = new HashMap<>();
        districtIds.put("Feldkirchen", "210");
        districtIds.put("Hermagor", "203");
        districtIds.put("Klagenfurt", "201");
        districtIds.put("Klagenfurt Land", "304");
        districtIds.put("Sankt Veit an der Glan", "205");
        districtIds.put("Spittal an der Drau", "206");
        districtIds.put("Villach", "202");
        districtIds.put("Villach Land", "207");
        districtIds.put("Völkermarkt", "208");
        districtIds.put("Wolfsberg", "209");
        return districtIds;
    }

    private Map<String, String> getDistrictIdsForNiederoesterreich() {
        Map<String, String> districtIds = new HashMap<>();
        districtIds.put("Amstetten", "301");
        districtIds.put("Baden", "306");
        districtIds.put("Bruck an der Leitha", "307");
        districtIds.put("Gänserndorf", "308");
        districtIds.put("Gmünd", "309");
        districtIds.put("Hollabrunn", "310");
        districtIds.put("Horn", "311");
        districtIds.put("Korneuburg", "312");
        districtIds.put("Krems an der Donau (Stadt)", "313");
        districtIds.put("Krems-Land", "314");
        districtIds.put("Lilienfeld", "315");
        districtIds.put("Melk", "316");
        districtIds.put("Mistelbach", "317");
        districtIds.put("Mödling", "318");
        districtIds.put("Neunkirchen", "320");
        districtIds.put("St. Pölten (Stadt)", "321");
        districtIds.put("St. Pölten-Land", "322");
        districtIds.put("Scheibbs", "323");
        districtIds.put("Tulln", "324");
        districtIds.put("Waidhofen an der Thaya", "325");
        districtIds.put("Waidhofen an der Ybbs (Stadt)", "326");
        districtIds.put("Wiener Neustadt (Stadt)", "329");
        districtIds.put("Wiener Neustadt-Land", "330");
        districtIds.put("Zwettl", "332");
        return districtIds;
    }

    private Map<String, String> getDistrictIdsForOberoesterreich() {
        Map<String, String> districtIds = new HashMap<>();
        districtIds.put("Braunau am Inn", "404");
        districtIds.put("Eferding", "405");
        districtIds.put("Freistadt", "406");
        districtIds.put("Gmunden", "407");
        districtIds.put("Grieskirchen", "408");
        districtIds.put("Kirchdorf an der Krems", "409");
        districtIds.put("Linz", "401");
        districtIds.put("Linz Land", "410");
        districtIds.put("Perg", "411");
        districtIds.put("Ried im Innkreis", "412");
        districtIds.put("Rohrbach", "413");
        districtIds.put("Schärding", "414");
        districtIds.put("Steyr", "402");
        districtIds.put("Steyr-Land", "415");
        districtIds.put("Urfahr-Umgebung", "416");
        districtIds.put("Vöcklabruck", "417");
        districtIds.put("Wels", "403");
        districtIds.put("Wels-Land", "418");
        return districtIds;
    }

    private Map<String, String> getDistrictIdsForSalzburg() {
        Map<String, String> districtIds = new HashMap<>();
        districtIds.put("Hallein", "502");
        districtIds.put("Salzburg Stadt", "501");
        districtIds.put("Salzburg-Umgebung", "503");
        districtIds.put("Sankt Johann im Pongau", "504");
        districtIds.put("Tamsweg", "505");
        districtIds.put("Zell am See", "506");
        return districtIds;
    }

    private Map<String, String> getDistrictIdsForSteiermark() {
        Map<String, String> districtIds = new HashMap<>();
        districtIds.put("Bruck-Mürzzuschlag", "621");
        districtIds.put("Deutschlandsberg", "603");
        districtIds.put("Graz", "601");
        districtIds.put("Graz-Umgebung", "606");
        districtIds.put("Hartberg-Fürstenfeld", "622");
        districtIds.put("Leibnitz", "610");
        districtIds.put("Leoben", "611");
        districtIds.put("Liezen", "612");
        districtIds.put("Murau", "614");
        districtIds.put("Murtal", "620");
        districtIds.put("Südoststeiermark", "623");
        districtIds.put("Voitsberg", "616");
        districtIds.put("Weiz", "617");
        return districtIds;
    }

    private Map<String, String> getDistrictIdsForTirol() {
        Map<String, String> districtIds = new HashMap<>();
        districtIds.put("Imst", "702");
        districtIds.put("Innsbruck", "701");
        districtIds.put("Innsbruck-Land", "703");
        districtIds.put("Kitzbühel", "704");
        districtIds.put("Kufstein", "705");
        districtIds.put("Landeck", "706");
        districtIds.put("Lienz", "707");
        districtIds.put("Reutte", "708");
        districtIds.put("Schwaz", "709");
        return districtIds;
    }

    private Map<String, String> getDistrictIdsForWien() {
        Map<String, String> districtIds = new HashMap<>();
        districtIds.put("01. Bezirk, Innere Stadt", "117223");
        districtIds.put("02. Bezirk, Leopoldstadt", "117224");
        districtIds.put("03. Bezirk, Landstraße", "117225");
        districtIds.put("04. Bezirk, Wieden", "117226");
        districtIds.put("05. Bezirk, Margareten", "117227");
        districtIds.put("06. Bezirk, Mariahilf", "117228");
        districtIds.put("07. Bezirk, Neubau", "117229");
        districtIds.put("08. Bezirk, Josefstadt", "117230");
        districtIds.put("09. Bezirk, Alsergrund", "117231");
        districtIds.put("10. Bezirk, Favoriten", "117232");
        districtIds.put("11. Bezirk, Simmering", "117233");
        districtIds.put("12. Bezirk, Meidling", "117234");
        districtIds.put("13. Bezirk, Hietzing", "117235");
        districtIds.put("14. Bezirk, Penzing", "117236");
        districtIds.put("15. Bezirk, Rudolfsheim-Fünfhaus", "117237");
        districtIds.put("16. Bezirk, Ottakring", "117238");
        districtIds.put("17. Bezirk, Hernals", "117239");
        districtIds.put("18. Bezirk, Währing", "117240");
        districtIds.put("19. Bezirk, Döbling", "117241");
        districtIds.put("20. Bezirk, Brigittenau", "117242");
        districtIds.put("21. Bezirk, Floridsdorf", "117243");
        districtIds.put("22. Bezirk, Donaustadt", "117244");
        districtIds.put("23. Bezirk, Liesing", "117245");
        return districtIds;
    }

    private Map<String, String> getDistrictIdsForVorarlberg() {
        Map<String, String> districtIds = new HashMap<>();
        districtIds.put("Bregenz", "802");
        districtIds.put("Dornbirn", "803");
        districtIds.put("Feldkirch", "804");
        districtIds.put("Bludenz", "801");
        return districtIds;
    }


    // Methoden für Bezirke

    private List<String> getDistrictsForWien() {
        return List.of(
                "01. Bezirk, Innere Stadt",
                "02. Bezirk, Leopoldstadt", "03. Bezirk, Landstraße", "04. Bezirk, Wieden", "05. Bezirk, Margareten",
                "06. Bezirk, Mariahilf", "07. Bezirk, Neubau", "08. Bezirk, Josefstadt", "09. Bezirk, Alsergrund",
                "10. Bezirk, Favoriten", "11. Bezirk, Simmering", "12. Bezirk, Meidling", "13. Bezirk, Hietzing",
                "14. Bezirk, Penzing", "15. Bezirk, Rudolfsheim-Fünfhaus", "16. Bezirk, Ottakring", "17. Bezirk, Hernals",
                "18. Bezirk, Währing", "19. Bezirk, Döbling", "20. Bezirk, Brigittenau", "21. Bezirk, Floridsdorf",
                "22. Bezirk, Donaustadt", "23. Bezirk, Liesing"
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