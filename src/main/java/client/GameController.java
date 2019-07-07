package client;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import util.cards.Card;
import util.cards.CardRank;
import util.cards.CardSuite;
import util.protocol.DataPacket;


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

        /*Card c = new Card(CardSuite.CLUBS, CardRank.KING);
        hBox.getChildren().add(new ImageView(c.getImage()));*/

        Card[] testHand = new Card[4];

        testHand[0] = new Card(CardSuite.CLUBS, CardRank.KING);
        testHand[1] = new Card(CardSuite.DIAMONDS, CardRank.KING);
        testHand[2] = new Card(CardSuite.HEARTS, CardRank.KING);
        testHand[3] = new Card(CardSuite.SPADES, CardRank.KING);

        setHand(testHand);

        Card ca = new Card(CardSuite.SPADES, CardRank.QUEEN);
        currentCard.setImage(ca.getImage());

    }

    /**
     * Sets the view according to a packet sent from the server
     * @param packet A packet describing the game state
     */
    public void setGameState(DataPacket packet) {

    }

    /**
     * Sets the player's displayed hand.
     * @param playerHand Array of cards
     */
    public void setHand(Card[] playerHand) {
        ObservableList<Node> children = hBox.getChildren();

        children.clear();

        for (Card c : playerHand) {
            children.add(new ImageView(c.getImage()));
        }
    }

    // Buttonactions for the whish cardSuite buttons
    @FXML
    public void spadesClicked() {
        System.out.println("You've wished spades");

    }
    @FXML
    public void heartsClicked() {
        System.out.println("You've wished hearts");

    }
    @FXML
    public void clubsClicked() {
        System.out.println("You've wished clubs");

    }
    @FXML
    public void diamondsClicked() {
        System.out.println("You've wished diamonds");

    }

}
