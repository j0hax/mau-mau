package util.game;

import util.cards.Card;

/**
 * Represents the current state of the game for an individual player
 */
public class GameState {

    private Card[] hand = new Card[32];
    private int activePlayerIndex = 0;

    /**
     * @param activePlayer the index of the currently active player (from String[] allPlayers in class NewGame)
     * @param hand         the individual players current hand
     */
    public GameState(int activePlayer, Card[] hand) {
        this.activePlayerIndex = activePlayerIndex;
        this.hand = hand;
    }

    /**
     * @return The hand of the current player
     */
    public Card[] getHand() {
        return hand;
    }

    /**
     * @return The index of the player who has the turn
     */
    public int activePlayerIndex() {
        return activePlayerIndex;
    }
}
