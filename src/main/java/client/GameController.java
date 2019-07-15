package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.cards.Card;
import util.cards.CardRank;
import util.cards.CardSuite;
import util.game.GameState;


public class GameController {

    private Client client;
    private String[] allPlayerNames;
    private Label[] allPlayerLabels;

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
    public Label turnLabel;
    @FXML
    public ImageView currentCard;
    @FXML
    public ImageView deck;

    @FXML
    public void initialize() {
        Card c = new Card(CardSuite.CLUBS, CardRank.KING);
        //Card[] cA = {new Card(CardSuite.CLUBS, CardRank.KING)};
        //setHand(cA);
        hBox.getChildren().add(new ImageView(c.getImage()));
        spadesButton.setCursor(Cursor.HAND);
        heartsButton.setCursor(Cursor.HAND);
        clubsButton.setCursor(Cursor.HAND);
        diamondsButton.setCursor(Cursor.HAND);
        allPlayerLabels = new Label[]{player2Cards, player3Cards, player4Cards};
        deck.setImage(new Image("client/cards/PNG-cards-1.3/back.png"));
        deck.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("User clicked on card back");
            client.requestCard();
            event.consume();
        });
    }

    /**
     * Sets the view according to a packet sent from the server
     * @param gameState A packet describing the game state
     */
    private void updateGame(GameState gameState) {
        currentCard.setImage(gameState.getLastPlaced().getImage());
        setHand(gameState.getHand());
        Integer[] numberOfCards = gameState.getNumberOfCards();
        int playerIndex = 0;

        for (int handIndex = 0; handIndex < numberOfCards.length; handIndex++) {
            if (handIndex != client.getID()) {
                allPlayerLabels[playerIndex].setText(allPlayerNames[handIndex] +
                        " - cards left: " + numberOfCards[handIndex]);
                ++playerIndex;
            }
        }

        if (gameState.activePlayerIndex() == client.getID()) {
            turnLabel.setText("Turn: You");
        } else {
            turnLabel.setText("Not your Turn!");
        }
    }

    /**
     * Sets the player's displayed hand.
     * @param playerHand Array of cards
     */
    private void setHand(Card[] playerHand) {
        hBox.getChildren().clear();

        //LinkedList<Card> hand = new LinkedList<>(Arrays.asList(playerHand));
        //Collections.sort(hand);

        for (Card c : playerHand) {
            ImageView im = new ImageView(c.getImage());
            im.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                System.out.println("User clicked on " + c.toString());
                client.layCard(c);
                event.consume();
            });
            hBox.getChildren().add(im);
        }
    }

    // Button actions for the wish cardSuite buttons
    @FXML
    public void spadesClicked() {
        client.wishCard(CardSuite.SPADES);
    }
    @FXML
    public void heartsClicked() {
        client.wishCard(CardSuite.HEARTS);
    }
    @FXML
    public void clubsClicked() {
        client.wishCard(CardSuite.CLUBS);
    }

    @FXML
    public void diamondsClicked() {
        client.wishCard(CardSuite.DIAMONDS);
    }

    private void openAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Game Over");

        if (client.getWinner() == client.getID()) {
            alert.setContentText("You are the winner!");
        } else {
            alert.setContentText("You lost:/ Player " +
                    client.getAllPlayerNames()[client.getWinner()] + " won!");
        }

        alert.showAndWait();
    }

    public void setClient(Client client) {
        this.client = client;
        allPlayerNames = client.getAllPlayerNames();
        updateGame(client.getCurrentGameState());
        this.client.getGameOverProperty().addListener((observable, oldValue,
                                                       newValue) -> {
            if (newValue) {
                Platform.runLater(() -> {
                    client.close();
                    openAlert();
                    Stage stage = (Stage) hBox.getScene().getWindow();
                    stage.close();
                });
                //System.exit(0);
            }
        });
        this.client.getHandUpdatedProperty().addListener((observable, oldValue,
                                                          newValue) -> {
            //System.out.println(newValue);
            if (newValue) {
                Platform.runLater(() -> {
                    //System.out.println("New hand is ready to be printed...");
                    updateGame(client.getCurrentGameState());
                    client.setHandUpdatedProperty(false);
                });
            }
        });
    }
}
