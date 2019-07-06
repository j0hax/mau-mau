package client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    public ImageView spadesButton;
    @FXML
    public ImageView heartsButton;
    @FXML
    public ImageView clubsButton;
    @FXML
    public ImageView diamondsButton;

    @FXML
    public Label player2Cards;
    @FXML
    public Label player3Cards;
    @FXML
    public Label player4Cards;

    @FXML
    public ImageView currentCard;
    @FXML
    public ImageView deck;




    @FXML
    public void initialize() {

        Card c = new Card(CardSuite.CLUBS, CardRank.KING);
        hBox.getChildren().add(new ImageView(c.getImage()));

        Card ca = new Card(CardSuite.SPADES, CardRank.QUEEN);
        currentCard.setImage(ca.getImage());

    }

    // Buttonactions for the whish cardSuite buttons
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
