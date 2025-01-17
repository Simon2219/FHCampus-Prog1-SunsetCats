package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ResultListCell extends ListCell<Immobilie>{

    private final ImageView imageView = new ImageView();
    private final Label price = new Label();
    private final Label zip = new Label();
    private final Label location = new Label();
    private final Label size = new Label();
    private final Label rooms = new Label();
    private final VBox content = new VBox();

    public ResultListCell() {
        super();
      //  imageView.setFitWidth(100);
      //  imageView.setFitHeight(100);

        content.getChildren().addAll(size,rooms,price,location);
        content.setSpacing(5);
    }

    @Override
    protected void updateItem(Immobilie immo, boolean empty) {
        super.updateItem(immo, empty);
        if (empty || immo == null) {
            setGraphic(null);
        } else {
          //  imageView.setImage(new Image(immo.getAttribute(Immobilie.AttributeKey.), true));
            size.setText(immo.getAttribute(Immobilie.AttributeKey.ESTATE_SIZE_TOTAL).toString() + " m²");
            rooms.setText(immo.getAttribute(Immobilie.AttributeKey.NUMBER_OF_ROOMS).toString() + " Zimmer");
            price.setText(immo.getAttribute(Immobilie.AttributeKey.PRICE).toString() + " €");
            location.setText(immo.getAttribute(Immobilie.AttributeKey.LOCATION).toString());
            setGraphic(content);
        }
    }
}
