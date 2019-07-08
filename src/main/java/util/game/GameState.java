package util.game;

import util.cards.Card;

public class GameState {

    private Card[] hand = new Card[32];
    private String activePlayer = "";

    public GameState(String activePlayer, Card[] hand) {
        this.activePlayer = activePlayer;
        this.hand = hand;
    }

    public Card[] getHand() {
        return hand;
    }

    public String getActivePlayer() {
        return activePlayer;
    }
}
