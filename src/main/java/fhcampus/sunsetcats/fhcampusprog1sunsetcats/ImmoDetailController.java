package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ImmoDetailController {
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
    private Label labelType;
    @FXML
    private Label labelFreeAreaType;
    @FXML
    private Label labelPublished;
    @FXML
    private Label labelVeroeffentlicht;
    @FXML
    private ImageView imageView;

    private Immobilie immobilie;

    public void initialize() {
        this.immobilie = ResultStore.getInstance().getSelectedImmo();
        updateDetails();
    }

    private void updateDetails() {
        if (immobilie != null) {
            // Map der Attribute und zugehörigen Labels
            Map<Immobilie.AttributeKey, Label> attributeLabelMap = Map.of(
                    Immobilie.AttributeKey.HEADING, labelHeading,
                    Immobilie.AttributeKey.BODY_DYN, labelDescription,
                    Immobilie.AttributeKey.PRICE, labelPrice,
                    Immobilie.AttributeKey.LOCATION, labelLocation,
                    Immobilie.AttributeKey.ESTATE_SIZE_TOTAL, labelEstateSize,
                    Immobilie.AttributeKey.NUMBER_OF_ROOMS, labelNumberOfRooms,
                    Immobilie.AttributeKey.IMMO_TYPE, labelImmoType,
                    Immobilie.AttributeKey.FLOOR, labelFloor,
                    Immobilie.AttributeKey.PUBLISHED_STRING, labelPublished,
                    Immobilie.AttributeKey.FREE_AREA_TYPE_NAME, labelFreeAreaType
            );

            // Setze den Typ (Miete/Eigentum) basierend auf dem gespeicherten Wert
            String immoType = ResultStore.getInstance().getSelectedType();
            // Aktualisiere labelType oder blende es aus
            if (immoType != null) {
                labelType.setText(immoType);
                labelType.setVisible(true);
            } else {
                labelType.setVisible(false);
            }

            // Alle Labels aktualisieren oder ausblenden
            attributeLabelMap.forEach((key, label) -> {
                Object value = immobilie.getAttribute(key);
                if (value != null && !value.toString().isEmpty()) {
                    label.setText(formatValue(key, value)); // Formatierung je nach Attribut
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
            });

            // Bild aktualisieren oder ausblenden
            String imageUrl = immobilie.getAttribute(Immobilie.AttributeKey.VIRTUAL_VIEW_LINK);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                imageView.setImage(new Image(imageUrl, true));
                imageView.setVisible(true);
            } else {
                imageView.setImage(null);
                imageView.setVisible(false);
            }

            // Setze das formatierte Veröffentlichungsdatum
            String publishedDate = immobilie.getAttribute(Immobilie.AttributeKey.PUBLISHED_STRING);
            if (immoType != null) {
                labelPublished.setText(formatPublishedDate(publishedDate));
                labelPublished.setVisible(true);
            } else {
                labelPublished.setVisible(false);
                labelVeroeffentlicht.setVisible(false);
            }
        } else {
            // Keine Immobilie ausgewählt: Alle Elemente ausblenden
            resetDetailView();
        }
    }

    public void goBack(ActionEvent event) {
        Navigation.loadContent("search-view.fxml");
    }

    public String formatPublishedDate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) {
            return null; // Rückgabe eines Standardwerts, wenn das Datum nicht verfügbar ist
        }
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(isoDate); // Konvertiere den ISO 8601 String in ein OffsetDateTime Objekt
        ZonedDateTime zonedDateTime = offsetDateTime.atZoneSameInstant(ZoneId.systemDefault()); // Wandle das OffsetDateTime in die lokale Zeit um
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // Definiere das gewünschte Format für Datum und Uhrzeit

        return zonedDateTime.format(dateFormatter); // Formatiere das Datum und die Uhrzeit
    }

    // Methode zur Formatierung des Preises und der Fläche
    public String formatDoubleValue(double doubleValue) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);
        numberFormat.setMinimumFractionDigits(2);  // Mindestens 2 Dezimalstellen
        numberFormat.setMaximumFractionDigits(2);  // Höchstens 2 Dezimalstellen
        return numberFormat.format(doubleValue);
    }

    // Formatierung je nach Attribut
    private String formatValue(Immobilie.AttributeKey key, Object value) {
        if (key == Immobilie.AttributeKey.PRICE || key == Immobilie.AttributeKey.ESTATE_SIZE_TOTAL) {
            try {
                return formatDoubleValue(Double.parseDouble(value.toString()));
            } catch (NumberFormatException e) {
                return "Ungültiger Wert";
            }
        } else if (key == Immobilie.AttributeKey.PUBLISHED_STRING) {
            return formatPublishedDate(value.toString());
        }
        return value.toString();
    }

    // Alle UI-Elemente zurücksetzen
    private void resetDetailView() {
        List<Label> labels = List.of(
                labelHeading, labelDescription, labelPrice, labelLocation,
                labelEstateSize, labelNumberOfRooms, labelImmoType, labelFloor, labelPublished
        );
        labels.forEach(label -> label.setVisible(false));
        imageView.setVisible(false);
    }

}
