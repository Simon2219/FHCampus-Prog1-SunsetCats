package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

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
    private Button goBack; // Zurück-Button
    @FXML
    private ImageView imageView; // Deklaration des ImageView

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
            Object imageUrl = immobilie.getAttribute(Immobilie.AttributeKey.VIRTUAL_VIEW_LINK);
            if (imageUrl != null && !imageUrl.toString().isEmpty()) {
                imageView.setImage(new Image(imageUrl.toString(), true));
                imageView.setVisible(true);
            } else {
                imageView.setImage(null);
                imageView.setVisible(false);
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
            return ""; // Rückgabe eines Standardwerts, wenn das Datum nicht verfügbar ist
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

    // Methode zur Formatierung des Preises und der Fläche
    public String formatDoubleValue(double doubleValue) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);
        numberFormat.setMinimumFractionDigits(2);  // Mindestens 2 Dezimalstellen
        numberFormat.setMaximumFractionDigits(2);  // Höchstens 2 Dezimalstellen
        return numberFormat.format(doubleValue);
    }

    public void setImmobilie(Immobilie immobilie) {
        this.immobilie = ResultStore.getInstance().getSelectedImmo();
        System.out.println("Immobilie " + immobilie + " selected");
        updateDetails();
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

    private String parseRoomsBucket(String bucketValue) {
        if (bucketValue == null || !bucketValue.matches("\\d+X\\d+")) {
            throw new IllegalArgumentException("Ungültiger Wert für NO_OF_ROOMS_BUCKET: " + bucketValue);
        }
        // Teilt "3X3" in ["3", "3"]
        String[] parts = bucketValue.split("X");
        return parts[0]; // Rückgabe der Raumanzahl
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


    /*
    private void updateDetails() {
        if (immobilie != null) {
            // labelId.setText(immobilie.getAttribute(Immobilie.AttributeKey.ID).toString());

            labelHeading.setText(immobilie.getAttribute(Immobilie.AttributeKey.HEADING).toString());
            labelDescription.setText(immobilie.getAttribute(Immobilie.AttributeKey.BODY_DYN).toString());

            // Preis formatieren und anzeigen
            double price = Double.parseDouble(immobilie.getAttribute(Immobilie.AttributeKey.PRICE).toString());
            labelPrice.setText(formatDoubleValue(price));

            labelLocation.setText(immobilie.getAttribute(Immobilie.AttributeKey.LOCATION).toString());

            // Fläche formatieren und anzeigen
            double estateSize = Double.parseDouble(immobilie.getAttribute(Immobilie.AttributeKey.ESTATE_SIZE_TOTAL).toString());
            labelEstateSize.setText(formatDoubleValue(estateSize));

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

     */
}
