package util.protocol.messages;

import util.cards.Card;

public class NewGame {

    private String[] allPlayers;
    private Integer[] numberOfCards;
    private int playerID;
    private Card[] initialHand;

    public NewGame(String[] allPlayers, Card[] initialHand, int id, Integer[] numberOfCards) {
        this.playerID = id;
        this.allPlayers = allPlayers;
        this.initialHand = initialHand;
        this.numberOfCards = numberOfCards;
    }

    public String[] getAllPlayers() {
        return allPlayers;
    }

    public Card[] getInitialHand() {
        return initialHand;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Integer[] getNumberOfCards() {
        return numberOfCards;
    }

}
