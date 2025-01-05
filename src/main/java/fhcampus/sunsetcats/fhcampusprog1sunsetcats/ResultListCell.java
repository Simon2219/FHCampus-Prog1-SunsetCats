package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ResultListCell extends ListCell<Immobilie>{

    private final ImageView imageView = new ImageView();
    private final Text idText = new Text();
    private final Text descriptionText = new Text();
    private final Text priceText = new Text();
    private final Text zipText = new Text();
    private final VBox content = new VBox();

    public ResultListCell() {
        super();
      //  imageView.setFitWidth(100);
      //  imageView.setFitHeight(100);

        content.getChildren().addAll(descriptionText,priceText);
        content.setSpacing(5);
    }

    @Override
    protected void updateItem(Immobilie immo, boolean empty) {
        super.updateItem(immo, empty);
        if (empty || immo == null) {
            setGraphic(null);
        } else {
            Double price = immo.getAttribute(Immobilie.AttributeKey.PRICE);
          //  Double zip = immo.getAttribute(Immobilie.AttributeKey.POSTCODE);
          //  imageView.setImage(new Image(immo.getAttribute(Immobilie.AttributeKey.), true));
          //  idText.setText(immo.getAttribute(Immobilie.AttributeKey.ID));
          //  zipText.setText(zip.toString());
            descriptionText.setText(immo.getAttribute(Immobilie.AttributeKey.DESCRIPTION));
            priceText.setText(price.toString());
            setGraphic(content);
        }
    }
}
