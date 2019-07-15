package util.protocol.messages;

import util.cards.Card;

public class NewGame {

    private String[] allPlayers;
    private Integer[] numberOfCards;
    private int playerID;
    private Card[] initialHand;
    private Card firstCard;

    public NewGame(String[] allPlayers, Card[] initialHand, int id,
                   Integer[] numberOfCards, Card firstCard) {
        this.playerID = id;
        this.allPlayers = allPlayers;
        this.initialHand = initialHand;
        this.numberOfCards = numberOfCards;
        this.firstCard = firstCard;
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

    public Card getFirstCard() {
        return firstCard;
    }

}
