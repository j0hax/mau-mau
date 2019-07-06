package client;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import util.cards.Card;
import util.cards.CardRank;
import util.cards.CardSuite;

public class GameController {

    @FXML
    public HBox hBox;

    @FXML
    public void initialize() {

        Card c = new Card(CardSuite.CLUBS, CardRank.KING);
        hBox.getChildren().add(new ImageView(c.getImage()));

    }

}
