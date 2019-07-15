package util.game;

import util.cards.Card;

/**
 * Represents the current state of the game for an individual player
 */
public class GameState {

    private int activePlayerIndex;
    private Integer[] numberOfCards;
    private Card[] hand;
    private Card lastPlaced;

    /**
     * @param activePlayer the index of the currently active player (from String[] allPlayers in class NewGame)
     * @param hand         the individual players current hand
     */
    public GameState(int activePlayer, Card[] hand, Integer[] numberOfCards,
                     Card lastPlaced) {
        this.activePlayerIndex = activePlayer;
        this.hand = hand;
        this.numberOfCards = numberOfCards;
        this.lastPlaced = lastPlaced;
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

    public Integer[] getNumberOfCards() {
        return numberOfCards;
    }

    public Card getLastPlaced() {
        return lastPlaced;
    }

}
