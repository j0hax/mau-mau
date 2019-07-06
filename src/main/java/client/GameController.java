package client;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import util.cards.Card;
import util.cards.CardRank;
import util.cards.CardSuite;

public class GameController {

    @FXML
    public HBox hBox;

    @FXML
    public ImageView spadesView;
    @FXML
    public ImageView heartsView;
    @FXML
    public ImageView clubsView;
    @FXML
    public ImageView diamondsView;


    @FXML
    public void initialize() {

        Card c = new Card(CardSuite.CLUBS, CardRank.KING);
        hBox.getChildren().add(new ImageView(c.getImage()));

    }

    @FXML
    public void spadesClicked(){
        System.out.println("You've wished spades");

    }
    @FXML
    public void heartsClicked(){
        System.out.println("You've wished hearts");

    }
    @FXML
    public void clubsClicked(){
        System.out.println("You've wished clubs");

    }
    @FXML
    public void diamondsClicked(){
        System.out.println("You've wished diamonds");

    }

}
