package util.game;

import util.cards.Card;

public class GameState {

    private Card[] hand = new Card[32];
    private String playerName = "";

    public GameState(String playerName, Card[] hand) {
        this.playerName = playerName;
        this.hand = hand;
    }


}
