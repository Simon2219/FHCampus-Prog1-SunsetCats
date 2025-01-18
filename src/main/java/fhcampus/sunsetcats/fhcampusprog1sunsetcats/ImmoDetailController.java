package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling.WillhabenConnector;
import fhcampus.sunsetcats.fhcampusprog1sunsetcats.Immobilie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

public class ImmoDetailController {
    @FXML
    private Label labelId;
    @FXML
    private Label labelHeading;
    @FXML
    private Label labelDescription;
    @FXML
    private Label labelPrice;
    @FXML
    private Label labelEstateSize;
    @FXML
    private Label labelLocation;
    @FXML
    private Label labelFloor;
    @FXML
    private Label labelImmoType;
    @FXML
    private Label labelNumberOfRooms;
    @FXML
    private Label labelPublished;

    @FXML
    private Button goBack; // Zurück-Button

    private Immobilie immobilie;

    @FXML
    private ImageView imageView; // Deklaration des ImageView

    public String formatPublishedDate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) {
            return "Unbekannt"; // Rückgabe eines Standardwerts, wenn das Datum nicht verfügbar ist
        }

        // Konvertiere den ISO 8601 String in ein OffsetDateTime Objekt
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(isoDate);

        // Wandle das OffsetDateTime in die lokale Zeit um
        ZonedDateTime zonedDateTime = offsetDateTime.atZoneSameInstant(ZoneId.systemDefault());

        // Definiere das gewünschte Format für Datum und Uhrzeit
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        // Formatiere das Datum und die Uhrzeit
        return zonedDateTime.format(dateFormatter);
    }

    // Methode zur Formatierung des Preises
    public String formatPrice(double price) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);
        numberFormat.setMinimumFractionDigits(2);  // Mindestens 2 Dezimalstellen
        numberFormat.setMaximumFractionDigits(2);  // Höchstens 2 Dezimalstellen
        return numberFormat.format(price);
    }

    // Methode zur Formatierung der Fläche
    public String formatEstateSize(double estateSize) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);
        numberFormat.setMinimumFractionDigits(2);  // Mindestens 2 Dezimalstellen
        numberFormat.setMaximumFractionDigits(2);  // Höchstens 2 Dezimalstellen
        return numberFormat.format(estateSize);
    }

    public void setImmobilie(Immobilie immobilie) {
        this.immobilie = ResultStore.getInstance().getSelectedImmo();
        System.out.println("Immobilie " + immobilie + " selected");
        updateDetails();
    }

    public void initialize() {
        this.immobilie = ResultStore.getInstance().getSelectedImmo();
        updateDetails();
    }

    public void goBack(ActionEvent event) {
        Navigation.loadContent("search-view.fxml");
    }

    private void updateDetails() {
        if (immobilie != null) {
            // labelId.setText(immobilie.getAttribute(Immobilie.AttributeKey.ID).toString());

            labelHeading.setText(immobilie.getAttribute(Immobilie.AttributeKey.HEADING).toString());
            labelDescription.setText(immobilie.getAttribute(Immobilie.AttributeKey.BODY_DYN).toString());

            // Preis formatieren und anzeigen
            double price = Double.parseDouble(immobilie.getAttribute(Immobilie.AttributeKey.PRICE).toString());
            labelPrice.setText(formatPrice(price));

            labelLocation.setText(immobilie.getAttribute(Immobilie.AttributeKey.LOCATION).toString());

            // Fläche formatieren und anzeigen
            double estateSize = Double.parseDouble(immobilie.getAttribute(Immobilie.AttributeKey.ESTATE_SIZE_TOTAL).toString());
            labelEstateSize.setText(formatEstateSize(estateSize));

            labelNumberOfRooms.setText(immobilie.getAttribute(Immobilie.AttributeKey.NUMBER_OF_ROOMS).toString());
            labelImmoType.setText(immobilie.getAttribute(Immobilie.AttributeKey.IMMO_TYPE).toString());
            labelFloor.setText(immobilie.getAttribute(Immobilie.AttributeKey.FLOOR).toString());

            // Setze das formatierte Veröffentlichungsdatum
            String publishedDate = immobilie.getAttribute(Immobilie.AttributeKey.PUBLISHED_STRING);
            labelPublished.setText(formatPublishedDate(publishedDate));

            String imageUrl = immobilie.getAttribute(Immobilie.AttributeKey.VIRTUAL_VIEW_LINK);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                imageView.setImage(new Image(imageUrl, true));
            } else {
                imageView.setImage(null); // Set to null if no image available
            }

            // TODO
            // weitere Details

        }
    }
}
