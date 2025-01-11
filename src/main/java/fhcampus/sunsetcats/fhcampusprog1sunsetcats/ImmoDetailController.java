package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling.WillhabenConnector;
import fhcampus.sunsetcats.fhcampusprog1sunsetcats.Immobilie;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    public void setImmobilie(Immobilie immobilie) {
        this.immobilie = immobilie;
        updateDetails();
    }

    @FXML
    public void initialize() {
        // Action für den Zurück-Button
        goBack.setOnAction(event -> {
            // Navigation zurück zur Suchergebnisseite
            Navigation.loadScene(goBack, "result-view.fxml");
        });
    }

    private void updateDetails() {
        if (immobilie != null) {
            labelId.setText(immobilie.getAttribute(Immobilie.AttributeKey.ID).toString());

            labelHeading.setText(immobilie.getAttribute(Immobilie.AttributeKey.HEADING).toString());
            labelDescription.setText(immobilie.getAttribute(Immobilie.AttributeKey.DESCRIPTION).toString());
            labelPublished.setText(immobilie.getAttribute(Immobilie.AttributeKey.PUBLISHED).toString());

            labelPrice.setText(immobilie.getAttribute(Immobilie.AttributeKey.PRICE).toString());
            labelLocation.setText(immobilie.getAttribute(Immobilie.AttributeKey.LOCATION).toString());
            labelEstateSize.setText(immobilie.getAttribute(Immobilie.AttributeKey.ESTATE_SIZE_TOTAL).toString());
            labelNumberOfRooms.setText(immobilie.getAttribute(Immobilie.AttributeKey.NUMBER_OF_ROOMS).toString());
            labelImmoType.setText(immobilie.getAttribute(Immobilie.AttributeKey.IMMO_TYPE).toString());
            labelFloor.setText(immobilie.getAttribute(Immobilie.AttributeKey.FLOOR).toString());

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
